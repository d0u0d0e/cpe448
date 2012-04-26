JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
	  DNALib.java \
	  Gene.java \
	  Main.java 

default: classes

classes: $(CLASSES:.java=.class)

c6: classes
	java Main fasta/contig6.txt gff/derecta_dot_contig6.0.gff

c10: classes
	java Main fasta/contig10.txt gff/derecta_dot_contig10.0.gff

t: classes
	java Main fasta/test.txt gff/test.gff

clean:
	$(RM) *.class
