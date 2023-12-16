import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.SharedPreferences
import com.crysure.reflvy.data.User

class UserViewModel : ViewModel() {
    val userLiveData: MutableLiveData<User> = MutableLiveData()

    fun updateUserData(sharedPreferences: SharedPreferences) {
        val updatedUser = User()
        updatedUser.loadFromSharedPreferences(sharedPreferences)
        userLiveData.postValue(updatedUser)
    }
}