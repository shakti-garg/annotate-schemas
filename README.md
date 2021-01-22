# annotate-schemas
This is an experimental repo on 
1) how to annotate binary schemas(protobuf) at field or message level
2) how to parse protobuf schemas

## Pre-requisites
1) Install protoc or buf(https://docs.buf.build/installation)

## Approach 1
Parse proto source files directly using wire-schema API

### Instructions
1) Run WireParser.java

### Positives
1) Ability to directly parse the plain-text proto file
2) Can extract comments also

### Negatives
1) Wire API's `ProtoParser` doesn't automatically load/merge imported schemas 

## Approach 2
Use protobuf API's FileDescriptorSet to parse protobuf file and its dependencies

### Instructions
1) Generate java bindings for proto file with definition of custom options
    ```
    protoc --proto_path=src/main/resources/proto --java_out=src/main/java  --experimental_allow_proto3_optional src/main/resources/proto/playground/options/v1/business_term_options.proto
    ```

2) Compile protobuf files to generate a compiled binary descriptor file(contains a FileDescriptorSet (a protocol buffer, 
   defined in descriptor.proto))
   ```
   protoc --proto_path=src/main/resources/proto --descriptor_set_out=src/main/resources/protoc-bin/business_term_options.desc --include_imports --experimental_allow_proto3_optional src/main/resources/proto/playground/options/v1/business_term_options.proto
   
   protoc --proto_path=src/main/resources/proto --descriptor_set_out=src/main/resources/protoc-bin/message_sample.desc --include_imports src/main/resources/proto/playground/v1/message_sample.proto
   ```
   
3) Run DescriptorFileParser.java
 
### Positives
1) Parser automatically loads imported schemas

### Negatives
1) Extra pre-processing step: compile proto files into a descriptor file
2) Uses Java bindings for Options file
2) Unable to parse comments


#Open Question
1) Validate WireParser
2) Remove java binding dependency for generating ExtensionRegistry in DescriptorFileParser
3) use buf to generate descriptor files instead of protoc