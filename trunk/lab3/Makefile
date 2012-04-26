JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
	  BM.java \
	  BioOverlap.java \
	  TestBM.java \
	  Combine.java

default: classes

classes: $(CLASSES:.java=.class)

combine: classes
	java Combine 

test: classes
	java TestBM 

clean:
	$(RM) *.class *.txt *.csv
