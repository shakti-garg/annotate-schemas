package org.github.shakti;

import com.google.protobuf.Descriptors;
import com.squareup.wire.schema.Location;
import com.squareup.wire.schema.internal.parser.*;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaReference;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;
import playground.v1.BusinessTermOptionsOuterClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaSchemaRegistryParser {

    public static void main(String[] args) throws IOException {
        //SchemaRegistryClient client = new CachedSchemaRegistryClient("localhost:8081", 100);

        Map<String, String> resolvedReferences = new HashMap<String, String>();
        resolvedReferences.put("playground/v1/name.proto",
                new String(Files.readAllBytes(Paths.get("src/main/resources/proto/playground/v1/name.proto"))));

        File schemaFile = new File("src/main/resources/proto/playground/v1/message_sample.proto");

        ProtobufSchema parsedSchema = parseProtobufSchema(schemaFile, Collections.emptyList(), resolvedReferences);
        System.out.println("canonical schema: "+parsedSchema.canonicalString());

        ProtoFileElement protoFileElement = parsedSchema.rawSchema();

        for (OptionElement optionElement : protoFileElement.getOptions()) {
            System.out.println("=====File Options======");
            System.out.println("name: " + optionElement.getName());
            System.out.println("value: " + optionElement.getValue());
        }

        Descriptors.Descriptor descriptor = parsedSchema.toDescriptor();
        printMessageSchema(descriptor);
    }

    private static ProtobufSchema parseProtobufSchema(File schemaFile, List<SchemaReference> references, Map<String, String> resolvedReferences) {
        try {
            return new ProtobufSchema(new String(Files.readAllBytes(schemaFile.toPath())),
                    references,
                    resolvedReferences,
                    (Integer)null,
                    (String)null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printMessageSchema(Descriptors.Descriptor msgType) {
        System.out.println("=====Message Schema======");
        System.out.println("name: " + msgType.getName());
        System.out.println("full name: " + msgType.getFields());

        for (Map.Entry<Descriptors.FieldDescriptor, Object> msgOption : msgType.getOptions().getAllFields().entrySet()) {
            System.out.println("=====Message Options======");
            System.out.println("name: " + msgOption.getKey().getName());

            if(msgOption.getValue() instanceof BusinessTermOptionsOuterClass.BusinessTermOptions){
                System.out.println("value def: " + ((BusinessTermOptionsOuterClass.BusinessTermOptions) msgOption.getValue()).getDef());
                System.out.println("value source_name: " + ((BusinessTermOptionsOuterClass.BusinessTermOptions) msgOption.getValue()).getSourceName());
                System.out.println("value source_uri: " + ((BusinessTermOptionsOuterClass.BusinessTermOptions) msgOption.getValue()).getSourceUri());
            }
            else {
                System.out.println("value: " + msgOption.getValue());
            }
        }

        System.out.println("=====Field Schema======");
        for (Descriptors.FieldDescriptor fieldType : msgType.getFields()) {
            System.out.println("name: " + fieldType.getName());
            System.out.println("type: " + fieldType.getType());
            System.out.println("full name: " + fieldType.getFullName());

            for (Map.Entry<Descriptors.FieldDescriptor, Object> fieldOption : fieldType.getOptions().getAllFields().entrySet()) {
                System.out.println("=====Field Options======");
                System.out.println("name: " + fieldOption.getKey().getName());
                System.out.println("value: " + fieldOption.getValue());
            }

            if(fieldType.getType() == Descriptors.FieldDescriptor.Type.MESSAGE){
                printMessageSchema(fieldType.getMessageType());
            }
            System.out.println("=====================");
        }
    }
}
