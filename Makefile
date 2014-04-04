setup:
	@./script/bootstrap

clean:
	@play clean

publish-local:
	@play clean publishLocal

publish:
	@play clean publish
