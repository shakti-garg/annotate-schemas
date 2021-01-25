package org.github.shakti;

import com.squareup.wire.schema.Location;
import com.squareup.wire.schema.internal.parser.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WireParser {
  private static final String BASE_LOCATION = "./";

  public static void main(String[] args) throws IOException {
    File schemaFile = new File("src/main/resources/proto/playground/v1/message_sample.proto");
    final String schemaString = new String(Files.readAllBytes(schemaFile.toPath()));

    Map<String, String> resolvedReferences = new HashMap<>();
    resolvedReferences.put("playground/v1/name.proto",
            new String(Files.readAllBytes(Paths.get("src/main/resources/proto/playground/v1/name.proto"))));

    ProtoFileElement protoFileElement = ProtoParser.Companion
        .parse(Location.get(BASE_LOCATION, "src/main/resources"), schemaString);

    printMetadata(protoFileElement);

    for(String nestedSchema : resolvedReferences.values()){
      ProtoFileElement nestedProtoFileElement = ProtoParser.Companion
              .parse(Location.get(BASE_LOCATION, "src/main/resources"), nestedSchema);

      printMetadata(nestedProtoFileElement);
    }
  }

  private static void printMetadata(ProtoFileElement protoFileElement) {
    System.out.println("=====Top-level Schema======");
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

}