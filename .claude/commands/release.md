Prepare and publish a new release of log4j2-log4Rich.

## Steps

1. **Determine version**: Read the current version from `pom.xml` line 10. Bump the patch version (e.g., 1.0.5 → 1.0.6) unless the user specifies a version.

2. **Update pom.xml**: Change the `<version>` tag to the new version.

3. **Build**: Run `mvn clean package` and verify all 4 jars are produced:
   - `log4j2-log4Rich-{version}.jar` (bridge only)
   - `log4j2-log4Rich.jar` (shaded fat jar with log4Rich core — recommended artifact)
   - `log4j2-log4Rich-{version}-sources.jar`
   - `log4j2-log4Rich-{version}-javadoc.jar`

4. **Run demo**: Compile and run `demo/Log4j2BridgeDemo.java` against the shaded jar to verify functionality:
   ```
   javac -cp target/log4j2-log4Rich.jar demo/Log4j2BridgeDemo.java -d /tmp/log4j2-demo
   java -cp /tmp/log4j2-demo:target/log4j2-log4Rich.jar demo.Log4j2BridgeDemo
   ```

5. **Commit and push**: Commit the version bump with message "Bump version to {version} for release" and push to origin/main.

6. **Create GitHub release**: Use `gh release create v{version}` with all 4 jar artifacts. Generate release notes from commits since the last release tag. Include an artifacts table in the notes.

7. **Verify**: Run `gh release view v{version}` to confirm the release is live.
