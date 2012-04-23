JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
	  BM.java \
	  BioBM.java \
	  Main.java \
	  Combine.java

default: classes

classes: $(CLASSES:.java=.class)

c: classes
	java Combine 

bm: classes
	java Main 

clean:
	$(RM) *.class
