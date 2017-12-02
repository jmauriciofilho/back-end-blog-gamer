package br.unifor.blog.database

import br.unifor.blog.entity.Entity
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.table.TableUtils
import java.lang.NullPointerException

object DaoFactory {

    var connSource:JdbcConnectionSource? = null

    inline fun <reified T:Entity,ID:Number> getDaoInstance():Dao<T, ID>{
        if(connSource == null) throw NullPointerException("\n"
                + "O JdbcConnectionSource deve ser inicializado.")
        return DaoManager.createDao(connSource, T::class.java) as Dao<T, ID>
    }

    inline fun <reified T:Entity> createTable(): Int {
        if(connSource == null) throw NullPointerException("\n"
                + "O JdbcConnectionSource deve ser inicializado.")
        return TableUtils.createTable(connSource, T::class.java)
    }

}