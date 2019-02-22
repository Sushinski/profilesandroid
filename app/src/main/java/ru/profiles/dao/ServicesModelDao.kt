package ru.profiles.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.profiles.model.*
import ru.profiles.model.CategoryModel
import ru.profiles.model.pojo.Location

@Dao
interface ServicesModelDao {

    @Transaction @Query("SELECT * FROM services")
    fun getServices(): LiveData<List<ServiceModel>>

    @Transaction
    fun saveServiceModel(serviceModel: ServiceModel){
        serviceModel.organization?.let{ serviceModel.organization_id = insert(it) }
        serviceModel.profile?.let{ serviceModel.profile_id = insertProfile(it) }
        serviceModel.ratings?.let{ serviceModel.ratings_id = insert(it)}
        serviceModel.categories?.let{
            saveCategoriesChildren(0, it)
        }
        serviceModel.locations.let{
             for(location in it){
                 insertLocation(location)
             }
        }
    }

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(serviceModel: ServiceModel) : Long

    @Transaction
    fun saveCategoriesChildren(parentId: Long, children: List<CategoryModel>){
        for (category: CategoryModel in children) {
            category.parentId = parentId
            val id = insert(category)
            saveCategoriesChildren(id, category.children)
        }
    }

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organizationModel: OrganizationModel) : Long

    @Transaction
    fun insertProfile(profileModel: ProfileModel): Long{
        profileModel.organization?.let{
            profileModel.organizationId = insert(it)
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

    @Transaction @Insert(onConflict = OnConflictStrategy.REPLACE)
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
}