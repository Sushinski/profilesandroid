package ru.profiles.dao

import android.app.Service
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import ru.profiles.model.*
import ru.profiles.model.CategoryModel
import ru.profiles.model.pojo.Location

@Dao
interface ServicesModelDao {

    @Query("SELECT * FROM services")
    fun getServices(): DataSource.Factory<Int, ServiceModel>

    @Transaction @Query("SELECT * FROM services WHERE title LIKE :searchString ORDER BY id ASC")
    fun searchServices(searchString: String): DataSource.Factory<Int, ServiceModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchModel: SearchModel)

    @Query("SELECT * FROM search WHERE searchTarget = :searchTarget ORDER BY id DESC LIMIT 1")
    fun getActualSearch(searchTarget: String): LiveData<SearchModel>

    @Query("SELECT title FROM services WHERE title LIKE :searchString")
    fun getSearchSuggestions(searchString: String): List<String>

    @Query("SELECT COUNT(*) FROM services WHERE id <= :id")
    fun getItemsCountForId(id: Long): Long

    @Transaction
    fun saveServiceModel(serviceModel: ServiceModel){
        /*serviceModel.organization?.let{ serviceModel.organization_id = insert(it) }
        serviceModel.profile?.let{ serviceModel.profile_id = insertProfile(it) }
        serviceModel.ratings?.let{ serviceModel.ratings_id = insert(it)}
        serviceModel.categories?.let{
            saveCategoriesChildren(null, it)
        }
        serviceModel.locations?.let{
             for(location in it){
                 insertLocation(location)
             }
        }*/
        val id = insert(serviceModel)
        Log.i("ProfilesInfo", "Service ${serviceModel.title} ${serviceModel.description} saved with $id")
    }

    @Transaction
    fun saveServicesList(list: List<ServiceModel>){
        for(item in list)
            saveServiceModel(item)
    }

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(serviceModel: ServiceModel) : Long

    @Transaction
    fun saveCategoriesChildren(parentId: Long?, children: List<CategoryModel>){
        for (category: CategoryModel in children) {
            category.parentId = parentId
            //Log.i("ProfilesInfo", "To insert category: id=${category.id} parentId=${category.parentId}")
            val id  = getCategory(category.id)?.id ?: insert(category)
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

    @Transaction @Query("SELECT * FROM category WHERE id=:categoryId")
    fun getCategory(categoryId: Long): CategoryModel?

    @Transaction
    fun insertProfile(profileModel: ProfileModel): Long{
        profileModel.organization?.let{
            profileModel.organizationId = insertOrganization(it)
        }
        profileModel.photo?.let{
            profileModel.photoId = insert(it)
        }
        profileModel.ratings?.let{
            profileModel.ratingsId = insert(it)
        }
        return insert(profileModel)
    }


    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoModel: PhotoModel) : Long


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