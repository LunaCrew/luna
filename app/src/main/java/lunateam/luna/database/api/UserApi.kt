package lunateam.luna.database.api

import lunateam.luna.database.db.InitDb
import lunateam.luna.database.entity.User
import lunateam.luna.model.UserData

class UserApi {
    private val userDao = InitDb().appDatabase.userDao()

    /**
     * Insert a new user into the database.
     * @param userData The user data to insert.
     * @see UserData
     */
    suspend fun insertUser(userData: UserData) {
        val user = User(
            userData.id,
            userData.email,
            userData.name
        )
        userDao?.insert(user)
    }

    /**
     * Get the user data from the database.
     * @return The user data if found, otherwise null.
     * @see UserData
     */
    fun getUser() {
        val user = userDao?.getUser()
        return user.let {
            if (it != null) {
                UserData(
                    it.id,
                    it.email,
                    it.name
                )
            }
        }
    }
}
