package br.unifor.blog.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "Posts")
class Post:Entity{

    @DatabaseField(generatedId = true)
    override var id: Long = 0

    @DatabaseField(columnName = "title", canBeNull = false)
    var title: String = ""

    @DatabaseField(columnName = "body", canBeNull = false)
    var body: String = ""

}