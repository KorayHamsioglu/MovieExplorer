package com.example.obssproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.obssproject.models.Movie


@Database(
    entities = [Movie::class],
    version = 1
)
abstract class MovieDatabase :RoomDatabase() {

    //room handles automatically
    abstract fun getMovieDAO(): MovieDao

    companion object{
        @Volatile
        private var instance: MovieDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance= it}
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
               context.applicationContext,
                MovieDatabase::class.java,
                "Favourite_Movies.db"
        ).build()
    }
}