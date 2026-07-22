JAVAC = javac
JAR = jar
JFLAGS = -d bin -sourcepath src
BIN = bin
LIB = lib/junit-platform-console-standalone-1.10.0.jar

all: s21_stack s21_queue s21_graph s21_graph_algorithms

s21_stack:
	@mkdir -p $(BIN)
	$(JAVAC) $(JFLAGS) src/s21_stack/Stack.java
	cd $(BIN) && $(JAR) cf s21_stack.jar s21_stack/*.class

s21_queue:
	@mkdir -p $(BIN)
	$(JAVAC) $(JFLAGS) src/s21_queue/Queue.java
	cd $(BIN) && $(JAR) cf s21_queue.jar s21_queue/*.class

s21_graph: s21_stack s21_queue
	@mkdir -p $(BIN)
	$(JAVAC) -cp $(BIN) $(JFLAGS) src/s21_graph/Graph.java src/s21_graph/GraphException.java
	cd $(BIN) && $(JAR) cf s21_graph.jar s21_graph/*.class

s21_graph_algorithms: s21_graph
	@mkdir -p $(BIN)
	$(JAVAC) -cp $(BIN) $(JFLAGS) src/s21_graph_algorithms/GraphAlgorithms.java
	cd $(BIN) && $(JAR) cf s21_graph_algorithms.jar s21_graph_algorithms/*.class

test: all
	@mkdir -p $(BIN)
	$(JAVAC) -cp "$(BIN);$(LIB)" -d $(BIN) src/tests/*.java
	java -cp "$(BIN);$(LIB)" org.junit.platform.console.ConsoleLauncher --scan-class-path

clean:
	rm -rf $(BIN)

run: all
	$(JAVAC) -cp "$(BIN)" -d $(BIN) src/app/ConsoleApp.java
	java -cp "$(BIN)" app.ConsoleApp

.PHONY: all clean test run s21_stack s21_queue s21_graph s21_graph_algorithms

