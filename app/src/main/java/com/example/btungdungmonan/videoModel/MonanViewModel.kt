package com.example.btungdungmonan.videoModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btungdungmonan.imgmonanrandomrcl1.Meal
import com.example.btungdungmonan.imgmonanrandomrcl1.MonAnall
import com.example.btungdungmonan.retrofit.Retroifitinterface
import com.example.btungdungmonan.roomDatabase.DatabaseMeal
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonanViewModel(var databaseMeal: DatabaseMeal) : ViewModel() {

    private var livdata=MutableLiveData<Meal>()


    //www.themealdb.com/api/json/v1/1/lookup.php?i=5277
    // Hàm để lấy dữ liệu từ API và cập nhật giá trị LiveData
    fun getlivedata(id:String){
        // Gọi API sử dụng Retrofitinterface.monanApi.getViewApi(id)
        Retroifitinterface.monanApi.getViewApi(id).enqueue(object :Callback<MonAnall>{
            override fun onResponse(call: Call<MonAnall>, response: Response<MonAnall>) {
                // Kiểm tra xem có phản hồi từ API không
                if (response.body()!=null)
                {
                    // Lấy dữ liệu từ phản hồi và cập nhật giá trị LiveData
                    val data = response.body()!!.meals[0]
                    livdata.value=data
                }
                else
                    return
            }

            override fun onFailure(call: Call<MonAnall>, t: Throwable) {
                // Xử lý khi có lỗi kết nối hoặc lỗi từ API
               Log.d("AAA",t.message.toString())
            }
        })
    }
    // Hàm để quan sát LiveData kiểu Meal
    fun obslivedata():LiveData<Meal>{
        return livdata
    }

    // update
    fun updateDataMeal(meal: Meal){
        // Sử dụng viewModelScope để tạo một coroutine scope liên quan đến ViewModel.
        viewModelScope.launch {
            // Gọi phương thức updateMeal() trên đối tượng MealDao trong cơ sở dữ liệu.
            // Cách triển khai của phương thức này phụ thuộc vào cách bạn định nghĩa MealDao.
            databaseMeal.mealDao().updateMeal(meal)
        }
    }
    // inser dulieu
    fun insertDataMeal(meal: Meal){
        viewModelScope.launch {
            databaseMeal.mealDao().insertMeal(meal)
        }
    }
    //deletedu lieu
    fun deleteDataMeal(meal: Meal){
        viewModelScope.launch {
            databaseMeal.mealDao().deleteMeal(meal)
        }
    }



}