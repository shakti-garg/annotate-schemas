package org.github.shakti;

import com.squareup.wire.schema.internal.parser.FieldElement;
import com.squareup.wire.schema.internal.parser.MessageElement;
import com.squareup.wire.schema.internal.parser.OptionElement;
import com.squareup.wire.schema.internal.parser.TypeElement;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaReference;
import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class WireParser {
    public static void main(String[] args) {
        ProtobufSchema parsedSchema = (ProtobufSchema) parseProtobufSchema(new File("src/main/resources/playground/v1/quote_sample_1.proto"),
                Collections.emptyList());
        System.out.println("=====Canonical Schema======");
        System.out.println(parsedSchema.canonicalString());

        for(OptionElement optionElement : parsedSchema.rawSchema().getOptions()) {
            System.out.println("=====File Options======");
            System.out.println("name: " + optionElement.getName());
            System.out.println("value: " + optionElement.getValue());
        }

        for(TypeElement msgType : parsedSchema.rawSchema().getTypes()){
            System.out.println("=====Message Schema======");
            System.out.println("name: " + msgType.getName());
            System.out.println("doc: " + msgType.getDocumentation());

            for(OptionElement optionElement : msgType.getOptions()) {
                System.out.println("=====Message Options======");
                System.out.println("name: " + optionElement.getName());
                System.out.println("value: " + optionElement.getValue());
            }

            System.out.println("=====Field Schema======");
            for(FieldElement fieldType : ((MessageElement) msgType).getFields()) {
                System.out.println("name: " + fieldType.getName());
                System.out.println("name: " + fieldType.getType());
                System.out.println("doc: " + fieldType.getDocumentation());

                for(OptionElement optionElement : fieldType.getOptions()) {
                    System.out.println("=====Field Options======");
                    System.out.println("name: " + optionElement.getName());
                    System.out.println("value: " + optionElement.getValue());
                }
                System.out.println("=====================");
            }
        }
    }

    private static ParsedSchema parseProtobufSchema(File schemaFile, List<SchemaReference> references) {
        try {
            return new ProtobufSchema(new String(Files.readAllBytes(schemaFile.toPath())),
                    references,
                    Collections.emptyMap(),
                    (Integer)null,
                    (String)null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}