# A template C++ Makefile for your SAT solver.

# Debugging flags
FLAGS=-m32 -Wall -Wold-style-cast -Wformat=2 -ansi -pedantic -ggdb3 -DDEBUG

# Optimizing flags
#FLAGS=-Wall -Wold-style-cast -Wformat=2 -ansi -pedantic -O3

# List all the .o files you need to build here
OBJS=OMNeTPipe.o main.o OMNeTPk.o Sockets.o

# This is the name of the executable file that gets built.  Please
# don't change it.
EXENAME=pipes_test

# Compile targets
all: $(OBJS)
	g++ $(FLAGS) $(OBJS) -lz -o $(EXENAME)
OMNeTPk.o: OMNeTPk.cc OMNeTPk.h
	g++ $(FLAGS) -c OMNeTPk.cc
main.o: main.cpp
	g++ $(FLAGS) -c main.cpp
OMNeTPipe.o: OMNeTPipe.cc OMNeTPipe.h
	g++ $(FLAGS) -c OMNeTPipe.cc
Sockets.o: Sockets.cc Sockets.h
	g++ $(FLAGS) -c Sockets.cc
# Add more compilation targets here



# The "phony" `clean' compilation target.  Type `make clean' to remove
# your object files and your executable.
.PHONY: clean
clean:
	rm -rf $(OBJS) $(EXENAME)
