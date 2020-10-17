package com.mrwaseem.firstcodeinterview.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mr.waseem.firstcode.Data.Model.PostsModel

@Dao
interface PostsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: List<PostsModel>)

    @Query("select * from posts_table")
    fun getPosts(): List<PostsModel>

    @Query("DELETE FROM posts_table")
    fun deleteAll()

    @Query("DELETE FROM posts_table where id =:id ")
    fun deleteFormPostId(id: Int)

}