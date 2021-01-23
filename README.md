# annotate-schemas
This is an experimental repo on 
1) how to annotate binary schemas(protobuf) at field or message level [by using custom options]
2) how to parse protobuf schemas [message and field-level attributes + options]

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
   
   or
   
    ```
    buf generate --config '{"version":"v1beta1","build":{"roots":["src/main/resources/proto"]}}' --template '{"version":"v1beta1","plugins":[{"name":"java","out":"src/main/java"}]}' --path src/main/resources/proto/playground/options/v1/business_term_options.proto
    ```

2) Compile protobuf files to generate a compiled binary descriptor file(contains a FileDescriptorSet (a protocol buffer, 
   defined in descriptor.proto))
   ```   
   protoc --proto_path=src/main/resources/proto --descriptor_set_out=src/main/resources/protoc-bin/message_sample.desc --include_imports src/main/resources/proto/playground/v1/message_sample.proto
   ```
   
   or
   
   ```
   buf build --config '{"version":"v1beta1","build":{"roots":["src/main/resources/proto"]}}' --exclude-source-info -o src/main/resources/buf-bin/message_sample.desc --path src/main/resources/proto/playground/v1/message_sample.proto
   ```
   
   
3) Run DescriptorFileParser.java
 
### Positives
1) Parser automatically loads imported schemas

### Negatives
1) Extra pre-processing step: compile proto files into a descriptor file
2) compile-time dependency of Java bindings for custom options (to populate extensionRegistry) 
   - Not able to populate extensionRegistry from fieldDescriptors due to issue(https://github.com/protocolbuffers/protobuf/issues/5587)
2) Unable to parse comments


## Other Approaches:
1) Dymamically load java bindings [Not considered as it uses reflection]
2) Read from Kafka Schema Registry [Not considered as it creates heavy coupling with schema-registry]

