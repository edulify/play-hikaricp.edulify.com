setup:
	@./script/bootstrap

clean:
	@sbt clean
	@rm -rf target
	@rm -rf project/target
	@rm -rf project/project

publish-local: clean
	@sbt +publishLocal

publish: clean
	@sbt publishSigned

idea: clean
	@sbt gen-idea

test: clean
	@./script/test