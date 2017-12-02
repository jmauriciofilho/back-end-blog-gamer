package br.unifor.blog.entity

import com.google.gson.annotations.Expose
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "posts")
class Post:Entity{

    @Expose
    @DatabaseField(generatedId = true)
    override var id: Long = 0

    @Expose
    @DatabaseField(columnName = "title", canBeNull = false)
    var title: String = ""

    @Expose
    @DatabaseField(columnName = "body", canBeNull = false)
    var body: String = ""

}