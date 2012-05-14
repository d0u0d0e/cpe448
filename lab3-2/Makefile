UI= UI/*.java
LIB= lib/*.java
ALL= $(UI) \
     $(LIB) \
     *.java			
     
CC=javac
ENGINE=java
JFLAGS=-deprecation #-Xlint


default: classes

classes: $(ALL)
	@echo "------------------"
	$(CC) $(JFLAGS) $^ 
	@echo "------------------"

run:
	@echo "running..."
	$(ENGINE) UI/BioGUI
	@echo "finished running."

BioGUI.java:

rep: classes
	java STmain

clean:
	@echo "-------------------------"
	@echo "*** Cleaning Files..."
	rm -f UI/*.class
	rm -f lib/*.class
	rm -f *.class
	@echo "--------------------"
