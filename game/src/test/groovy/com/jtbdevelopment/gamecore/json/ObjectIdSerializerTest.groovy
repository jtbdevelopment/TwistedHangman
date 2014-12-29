package com.jtbdevelopment.gamecore.json

import com.fasterxml.jackson.core.JsonGenerator
import com.jtbdevelopment.gamecore.json.mongo.ObjectIdSerializer
import org.bson.types.ObjectId

/**
 * Date: 12/22/14
 * Time: 12:04 PM
 */
class ObjectIdSerializerTest extends GroovyTestCase {
    private ObjectIdSerializer serializer = new ObjectIdSerializer()

    public void testSerializesHexString() {
        ObjectId id = new ObjectId()
        def jgen = [
                writeString: {
                    String it ->
                        assert it == id.toHexString()
                }
        ] as JsonGenerator
        serializer.serialize(id, jgen, null)
    }
}
