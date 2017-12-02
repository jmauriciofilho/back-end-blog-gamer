package br.unifor.blog.entity

import com.google.gson.annotations.Expose
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "comments")
class Comment:Entity{

    @Expose
    @DatabaseField(generatedId = true)
    override var id: Long = 0

    @Expose
    @DatabaseField(columnName = "author", canBeNull = false)
    var author: String = ""

    @Expose
    @DatabaseField(columnName = "body", canBeNull = false)
    var body: String = ""

}