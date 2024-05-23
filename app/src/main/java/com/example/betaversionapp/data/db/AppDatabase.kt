package com.example.betaversionapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.db.entities.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDB.db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            instance?.getCategoryDao()?.insertCategory(Category(1, true, "Зарплата", "salary"))
                            instance?.getCategoryDao()?.insertCategory(Category(2, true, "Инвестиции", "investment"))
                            instance?.getCategoryDao()?.insertCategory(Category(3, true,"Неожиданные доходы", "windfall"))

                            instance?.getCategoryDao()?.insertCategory(Category(4, false, "Развлечения", "entertainments"))
                            instance?.getCategoryDao()?.insertCategory(Category(5, false, "Подарки", "gifts"))
                            instance?.getCategoryDao()?.insertCategory(Category(6, false, "Здоровье", "health"))
                            instance?.getCategoryDao()?.insertCategory(Category(7, false, "Бытовые товары", "home"))
                            instance?.getCategoryDao()?.insertCategory(Category(8, false, "Бытовая техника", "home_appliances"))
                            instance?.getCategoryDao()?.insertCategory(Category(9, false, "Интернет услуги", "internet_services"))
                            instance?.getCategoryDao()?.insertCategory(Category(10, false, "Продукты", "products"))
                            instance?.getCategoryDao()?.insertCategory(Category(11, false, "Кафе и рестораны", "restaraunt"))
                            instance?.getCategoryDao()?.insertCategory(Category(12, false, "Транспорт", "transport"))
                            instance?.getCategoryDao()?.insertCategory(Category(13, false, "Путешествия", "travelling"))
                            instance?.getCategoryDao()?.insertCategory(Category(14, false, "Одежда", "clothes"))
                        }
                    }
                })
                .build()

    }
}
