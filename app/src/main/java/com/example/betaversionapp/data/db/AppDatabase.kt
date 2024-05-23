package com.example.betaversionapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.db.entities.Transaction

@Database(
    entities = [Transaction::class, Category::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getCategoryDao(): CategoryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: kotlin.synchronized(LOCK) {
            instance ?: createDatabase(context).also { db ->
                instance = db
                db.getCategoryDao().insertCategory(Category(1, true, "Зарплата", R.id.salary))
                db.getCategoryDao().insertCategory(Category(2, true, "Инвестиции", R.id.investment))
                db.getCategoryDao().insertCategory(Category(3, true,"Неожиданные доходы", R.id.))
                db.getCategoryDao().insertCategory(Category(4, false, "Развлечения", R.id.entertainments))
                db.getCategoryDao().insertCategory(Category(5, false, "Подарки", R.id.gifts))
                db.getCategoryDao().insertCategory(Category(6, false, "Здоровье", R.id.health))
                db.getCategoryDao().insertCategory(Category(7, false, "Бытовые товары", R.id.home))
                db.getCategoryDao().insertCategory(Category(8, false, "Бытовая техника", R.id.home_appliances))
                db.getCategoryDao().insertCategory(Category(9, false, "Интернет услуги", R.id.internet_services))
                db.getCategoryDao().insertCategory(Category(10, false, "Продукты", R.id.products))
                db.getCategoryDao().insertCategory(Category(11, false, "Кафе и рестораны", R.id.restaurant))
                db.getCategoryDao().insertCategory(Category(12, false, "Транспорт", R.id.transport))
                db.getCategoryDao().insertCategory(Category(13, false, "Путешествия", R.id.travelling))
                db.getCategoryDao().insertCategory(Category(14, false, "Одежда", R.id.clothes))
            }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "AppDB.db").build()

    }
}
