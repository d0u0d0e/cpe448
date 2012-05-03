JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
	  BM.java \
	  BioOverlap.java \
	  TestBM.java \
	  Combine.java \
	  TestBM.java

default: classes

classes: $(CLASSES:.java=.class)

test: classes
	@echo "Running Tests..."
	java TestBM
	@echo "Finished Testing."

clean:
	$(RM) *.class *.txt *.csv
