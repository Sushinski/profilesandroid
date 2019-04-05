package ru.profiles.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import ru.profiles.model.*
import ru.profiles.model.CategoryModel
import ru.profiles.model.LocationModel
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

    @Query("SELECT * FROM category WHERE title LIKE :searchString")
    fun searchCategories(searchString: String): LiveData<Array<CategoryModel>>

    @Update
    fun update(model: ServiceModel)

    @Transaction
    fun saveServiceModel(serviceModel: ServiceModel){

        try {
            serviceModel.organization?.let {
                serviceModel.organization_id = insert(it)
            }
            serviceModel.profile?.let {
                serviceModel.profile_id = insertProfile(it)
            }
            serviceModel.ratings?.let {
                serviceModel.ratings_id = insert(it)
            }
            val sid = insert(serviceModel)
            serviceModel.files?.let{
                insertServicePhotos(sid, it)
            }
            /*serviceModel.categories?.let {
                saveCategoriesChildren(null, it)
                Log.i("ProfilesInfo", "Categories saved")
            }*/
            serviceModel.locationModels?.let {
                for (location in it) {
                    insertLocation(location)?.let{
                        lid->insert(ServiceLocationModel(sid, lid))
                    }
                }
            }
        }catch (e: Exception){
            Log.i("ProfilesInfo", "Failed create embedded objects: $e")
        }
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
            organizationModel.ratings_id = insert(it) ?: 0
        }
        val orgId = insert(organizationModel)
        organizationModel.areas?.also{
            for(area in it){
                insert(area)?.let {
                    aId-> insert(OrganizationArea(orgId, aId))
                }
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
    fun insertProfile(profileModel: ProfileModel): Long?{
        var id: Long? = profileModel.orig_id?.let { getProfileId(it) } ?: 0L
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
    fun insert(profileModel: ProfileModel) : Long?

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ratingsModel: RatingsModel) : Long?

    @Transaction @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(categoryModel: CategoryModel): Long?

    @Transaction
    fun insertLocation(locationModel: LocationModel): Long? {
        return locationModel.country?.let {
            getCountry(it.name ?: CountryModel.UNDEFINED) ?: insert(it)
        }?.let {
            locationModel.countryId = it
            locationModel.city?.let{ c->getCity(c.name ?: CityModel.UNDEFINED) ?: insert(c) }
        }?.let {
            locationModel.cityId = it
            insert(locationModel)
        }?.also {
            locationModel.metroStations?.let{
                list->for(st in list) {
                    getMetroStation(st.name ?: MetroStationModel.UNDEFINED) ?: insert(st) ?.let {
                        stId->insert(LocationMetroStation(it, stId))
                    }
                }
            }
        }
    }

    @Transaction @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(location: LocationModel): Long?

    @Transaction @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(city: CityModel) : Long?

    @Query ("SELECT id FROM city WHERE name=:cityName")
    fun getCity(cityName: String) : Long?

    @Transaction @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(metro: MetroStationModel) : Long?

    @Query("SELECT id FROM metro_station WHERE name=:stationName")
    fun getMetroStation(stationName: String): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(locationMetro: LocationMetroStation): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(serviceLocation: ServiceLocationModel)

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(area: AreaModel) : Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(country: CountryModel): Long?

    @Query("SELECT id FROM country WHERE name=:countryName")
    fun getCountry(countryName: String): Long?
}