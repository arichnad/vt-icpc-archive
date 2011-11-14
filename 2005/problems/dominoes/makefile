CPPPROG=dominoes
JAVAPROG=Dominoes
DIR=${shell pwd}
TESTDAT=$(wildcard test*.in)
TESTOUT=$(TESTDAT:%.in=%.out)
JTESTOUT=$(TESTDAT:%.in=%.jout)
DIFFS=$(TESTDAT:%.in=%.diff)

all: $(DIFFS)

out: $(TESTOUT)

outj: $(JTESTOUT)

clean: ; rm $(CPPPROG) *.class checkDominoes2


%.diff: %.out %.jout checkDominoes2
	echo $*
	./checkDominoes2 $*.jout $*.out $*.diff

%.out: $(CPPPROG) %.in
	$(CPPPROG) < $*.in > $*.out

%.jout: $(JAVAPROG).class %.in
	java $(JAVAPROG) < $*.in > $*.jout
	dos2unix $*.jout

checkDominoes2: checkDominoes2.cpp
	g++ -g -o checkDominoes2 checkDominoes2.cpp -lm

$(CPPPROG): $(CPPPROG).cpp
	g++ -g -o $(CPPPROG) $(CPPPROG).cpp -lm

$(JAVAPROG).class: $(JAVAPROG).java
	javac $(JAVAPROG).java
