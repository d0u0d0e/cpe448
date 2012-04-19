JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
	  BM.java \
	  Main.java

default: classes

classes: $(CLASSES:.java=.class)

t: classes
	java Main 

clean:
	$(RM) *.class
