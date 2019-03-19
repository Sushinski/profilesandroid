package ru.profiles.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import ru.profiles.model.*
import ru.profiles.model.CategoryModel
import ru.profiles.model.pojo.Location
import ru.profiles.model.pojo.PhotoFile

@Dao
interface ServicesModelDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction @Query("SELECT * FROM services WHERE title LIKE :searchString ORDER BY id ASC")
    fun searchServices(searchString: String): DataSource.Factory<Int, ServiceWithRelations>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchModel: SearchModel)

    @Query("SELECT * FROM search WHERE searchTarget = :searchTarget ORDER BY id DESC LIMIT 1")
    fun getActualSearch(searchTarget: String): LiveData<SearchModel>

    @Query("SELECT title FROM services WHERE title LIKE :searchString")
    fun getSearchSuggestions(searchString: String): List<String>

    @Query("SELECT COUNT(*) FROM services WHERE id <= :id")
    fun getItemsCountForId(id: Long): Long

    @Update
    fun update(model: ServiceModel)

    @Transaction
    fun saveServiceModel(serviceModel: ServiceModel){

        try {
            serviceModel.organization?.let {
                serviceModel.organization_id = insert(it)
                Log.i("ProfilesInfo", "Organization saved with ${serviceModel.organization_id}")
            }
            serviceModel.profile?.let {
                serviceModel.profile_id = insertProfile(it)
                Log.i("ProfilesInfo", "Profile saved with ${serviceModel.profile_id}")
            }
            serviceModel.ratings?.let {
                serviceModel.ratings_id = insert(it)
                    Log.i("ProfilesInfo", "Rating saved with ${serviceModel.ratings_id}")
            }
            /*serviceModel.categories?.let {
                saveCategoriesChildren(null, it)
                Log.i("ProfilesInfo", "Categories saved")
            }*/
            serviceModel.locations?.let {
                for (location in it) {
                    insertLocation(location)
                }
                Log.i("ProfilesInfo", "Locations saved")
            }
        }catch (e: Exception){
            Log.i("ProfilesInfo", "Failed create embedded objects: $e")
        }
        val id = insert(serviceModel)
        serviceModel.files?.let{
            insertServicePhotos(id, it)
        }
        Log.i("ProfilesInfo", "Service ${serviceModel.title} ${serviceModel.description} saved with $id")
    }

    @Transaction
    fun saveServicesList(list: List<ServiceModel>){
        for(item in list)
            saveServiceModel(item)
    }

    @Transaction
    fun insertServicePhotos(serviceId: Long, list: List<PhotoFile>){
        for(item in list){
            item.mFile.origId
            ?.let{ getPhotoIdByOrigId(it) }
            ?.let{ if(it > 0) it else insert(item.mFile) }
            ?.also { insert(ServicePhotoModel(serviceId = serviceId, photoId = it)) }
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(servicePhoto: ServicePhotoModel)

    @Transaction @Query("SELECT * FROM photo WHERE id = (SELECT photoId FROM service_photo WHERE serviceId = :serviceId)")
    fun getServicePhotos(serviceId: Long): List<PhotoModel>

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(serviceModel: ServiceModel) : Long

    @Transaction
    fun saveCategoriesChildren(parentId: Long?, children: List<CategoryModel>){
        for (category: CategoryModel in children) {
            category.parent_id = parentId
            //Log.i("ProfilesInfo", "To insert category: id=${category.id} parentId=${category.parentId}")
            val id  = getCategory(category.orig_id ?: 0)?.id ?: insert(category)
            category.children?.let{saveCategoriesChildren(id, it)}
        }
    }

    @Transaction
    fun insertOrganization(organizationModel: OrganizationModel) : Long{
        organizationModel.phone?.let {
            organizationModel.phone_id = insert(it)
        }
        organizationModel.ratings?.let {
            organizationModel.ratings_id = insert(it)
        }
        val orgId = insert(organizationModel)
        organizationModel.areas?.let{
            for(area in it){
                val areaId = insert(area)
                val oa = OrganizationArea(orgId, areaId)
                insert(oa)
            }
        }
        return orgId
    }

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(phoneModel: PhoneModel) : Long

    @Transaction @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(org_area: OrganizationArea)

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organizationModel: OrganizationModel) : Long

    @Transaction @Query("SELECT * FROM category WHERE orig_id=:categoryId")
    fun getCategory(categoryId: Long): CategoryModel?

    @Transaction
    fun insertProfile(profileModel: ProfileModel): Long{
        var id: Long = profileModel.orig_id?.let { getProfileId(it) } ?: 0L
        if(id == 0L){
            profileModel.organization?.let{
                    o->profileModel.organizationId = insertOrganization(o)
            }
            profileModel.photo?.also{
                    p->profileModel.photoId = insert(p)
                    Log.i("ProfilesInfo", "Photo inserted with ${profileModel.photoId}")
            }
            profileModel.ratings?.let{
                    r->profileModel.ratingsId = insert(r)
            }
            id = insert(profileModel)
        }
        return id
    }

    @Transaction @Query("SELECT id FROM profile WHERE orig_id = :origId")
    fun getProfileId(origId: Long) : Long

    @Transaction @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(photoModel: PhotoModel) : Long

    @Query("SELECT id FROM photo WHERE origId = :origId")
    fun getPhotoIdByOrigId(origId: String) : Long

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profileModel: ProfileModel) : Long

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ratingsModel: RatingsModel) : Long

    @Transaction @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(categoryModel: CategoryModel): Long

    @Transaction
    fun insertLocation(location: Location){
        insert(location.city)
        location.metroStations.let{
            for(station in it) insert(station)
        }
    }

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityModel) : Long

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(metro: MetroStationModel) : Long

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(area: AreaModel) : Long
}