package org.github.shakti;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import com.squareup.wire.schema.Location;
import com.squareup.wire.schema.ProtoFile;
import com.squareup.wire.schema.Schema;
import com.squareup.wire.schema.SchemaLoader;
import com.squareup.wire.schema.internal.parser.FieldElement;
import com.squareup.wire.schema.internal.parser.MessageElement;
import com.squareup.wire.schema.internal.parser.OptionElement;
import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.ProtoParser;
import com.squareup.wire.schema.internal.parser.TypeElement;

//import io.confluent.kafka.schemaregistry.ParsedSchema;
//import io.confluent.kafka.schemaregistry.client.rest.entities.SchemaReference;
//import io.confluent.kafka.schemaregistry.protobuf.ProtobufSchema;

public class WireParser {
  private static final String DEFAULT_LOCATION = "src/main/proto";

  public static void main(String[] args) throws IOException {
    final String path = "src/main/resources/playground/v1/quote_sample.proto";
    final File schemaFile = new File(path);

//      final SchemaLoader loader = new SchemaLoader();
//      loader.addSource(file);
//      final Schema schema = loader.load();
//      final List<ProtoFile> protoFiles = schema.getProtoFiles();

//    ProtobufSchema parsedSchema = (ProtobufSchema) parseProtobufSchema(
//        new File("src/main/resources/playground/v1/quote_sample.proto"),
//        Collections.emptyList());

    final String schemaString = new String(Files.readAllBytes(schemaFile.toPath()));
    final ProtoFileElement protoFileElement = ProtoParser.Companion
        .parse(Location.get(""), schemaString);

    System.out.println("=====Canonical Schema======");
    System.out.println(protoFileElement.toSchema());

    for (OptionElement optionElement : protoFileElement.getOptions()) {
      System.out.println("=====File Options======");
      System.out.println("name: " + optionElement.getName());
      System.out.println("value: " + optionElement.getValue());
    }

    for (TypeElement msgType : protoFileElement.getTypes()) {
      System.out.println("=====Message Schema======");
      System.out.println("name: " + msgType.getName());
      System.out.println("doc: " + msgType.getDocumentation());

      for (OptionElement optionElement : msgType.getOptions()) {
        System.out.println("=====Message Options======");
        System.out.println("name: " + optionElement.getName());
        System.out.println("value: " + optionElement.getValue());
      }

      System.out.println("=====Field Schema======");
      for (FieldElement fieldType : ((MessageElement) msgType).getFields()) {
        System.out.println("name: " + fieldType.getName());
        System.out.println("name: " + fieldType.getType());
        System.out.println("doc: " + fieldType.getDocumentation());

        for (OptionElement optionElement : fieldType.getOptions()) {
          System.out.println("=====Field Options======");
          System.out.println("name: " + optionElement.getName());
          System.out.println("value: " + optionElement.getValue());
        }
        System.out.println("=====================");
      }
    }
  }

//    private static ParsedSchema parseProtobufSchema(File schemaFile, List<SchemaReference> references) {
//        try {
//            return new ProtobufSchema(new String(Files.readAllBytes(schemaFile.toPath())),
//                    references,
//                    Collections.emptyMap(),
//                    (Integer)null,
//                    (String)null);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}