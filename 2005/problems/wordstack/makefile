CPPPROG=wordstack
JAVAPROG=WordStack1
DIR=${shell pwd}
TESTDAT=$(wildcard test*.in)
TESTOUT=$(TESTDAT:%.in=%.out)
JTESTOUT=$(TESTDAT:%.in=%.jout)
DIFFS=$(TESTDAT:%.in=%.diff)

all: $(DIFFS)

out: $(TESTOUT)

outj: $(JTESTOUT)

clean: ; rm $(CPPPROG) *.class


%.diff: %.out %.jout
	echo $*
	diff -b $*.out $*.jout | tee $*.diff

%.out: $(CPPPROG) %.in
	$(CPPPROG) < $*.in > $*.out

%.jout: $(JAVAPROG).class %.in
	java $(JAVAPROG) < $*.in > $*.jout

$(CPPPROG): $(CPPPROG).cpp
	g++ -g -o $(CPPPROG) $(CPPPROG).cpp -lm

$(JAVAPROG).class: $(JAVAPROG).java
	javac $(JAVAPROG).java
