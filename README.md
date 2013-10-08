maven-proto2docbook-plugin
==========================

Create docbook files from .proto files

Usage
-----

```xml
  <plugin>
   <groupId>es.amplia.research</groupId>
	<artifactId>proto2docbook-maven-plugin</artifactId>
	<version>1.1.0</version>
	<executions>
	   <execution>
		 <id>generate</id>
		 <phase>generate-sources</phase>
		 <goals>
			<goal>proto2docbook</goal>
		 </goals>
		</execution>
	</executions>
 </plugin>  
```

Optional Parameters
-------------------

*  **outdir**: Directory where docbook files will be generated  
*Default Value*: `${project.build.directory}`
* **indir**:  Directory where read proto files  
*Default Value:* `src/main/protobuf`
* **protoc**: Path to `protoc` command  
*Default Value:* `/usr/local/bin/protoc`
* **hierarchical**: Maintains directory hierarchy  
*Default Value:* `true`
