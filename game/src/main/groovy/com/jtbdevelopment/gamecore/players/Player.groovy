package com.jtbdevelopment.gamecore.players

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.jtbdevelopment.gamecore.mongo.json.ObjectIdDeserializer
import com.jtbdevelopment.gamecore.mongo.json.ObjectIdSerializer
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 11/3/14
 * Time: 6:53 AM
 */
//  TODO
//  This propagating to game table...
//@CompoundIndex(unique = true, name = "id_source", def = "{'sourceId':1, 'source':1}")
@Document(collection = "player")
@CompileStatic
@JsonIgnoreProperties(["idAsString"])
class Player extends AbstractPlayer<ObjectId> implements Cloneable {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    ObjectId id = new ObjectId()

    @Indexed
    private String md5

    String getIdAsString() {
        return id.toHexString()
    }

    void setId(final ObjectId id) {
        this.id = id
        computeMD5Hex()
    }

    protected String getMd5Internal() {
        return this.md5
    }
    protected void setMd5(final String md5) {
        this.md5 = md5
    }
}