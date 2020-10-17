package com.mrwaseem.firstcodeinterview.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mr.waseem.firstcode.Data.Model.PostsModel
import com.mrwaseem.firstcodeinterview.data.model.PostsDao
import com.ugb.mvvmkotlin.ApiClient.ApiClient

@Database(entities = arrayOf(PostsModel::class), version = 1)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
}

class DBManager {

        @Volatile
        private var instance: PostsDatabase? = null
        fun getDatabase(context: Context): PostsDatabase? {
            if (instance == null) {
                synchronized(PostsDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext, PostsDatabase::class.java, "myPostsDb"
                        ).allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return instance

        }



}
