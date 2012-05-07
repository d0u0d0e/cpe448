JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
	  BM.java \
	  BioOverlap.java \
	  Isoform.java \
	  Combine.java \
	  TestBM.java

default: classes

classes: $(CLASSES:.java=.class)

c1: classes
	java Combine c 20 21

c: classes
	java Combine c 20 38

f: classes
	java Combine f 22 42

test: classes
	@echo "Running Tests..."
	java TestBM
	@echo "Finished Testing."

clean:
	$(RM) *.class *.txt *.csv
