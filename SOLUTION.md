# Solution

Implementation of a hot topic analysis for RSS feeds using Spring Boot and embedded (in memory) H2 database.

## Technical Specification

Described solution uses:
    
    - Java 8 (jdk1.8.0_201)
    
    - Spring Boot - 2.1.6.RELEASE starter with additional artefacts
    
    - embedded H2 database - version 1.4.197
    
Besides these main dependencies, solution also uses some specific frameworks that will be discussed further. 
Solution is made using IntelliJ IDEA, and uses Maven as build tool. 


## Description of the solution

The emphasis in the solution is given to modularity (now, looking back, maybe even too much of modularity) and core/domain backbone. 

Domain/backbone consist of 3 logical pillars:

- FEED PROCESSING - fetching, reading and mapping into domain custom objects of obtained RSS feed structures
- DISMANTLING - dissasabmling RSS feeds into domain specific structures that can be further analysed/processed and/or stored
- ANALYSIS - that is split into two parts by the storing of the data to the DB

### Modularity

Modularity is expressed through abstraction in each of the 3 main domains. Idea was to enable easy switching of each custom implementation and allow different implementations/customizations for the each step in the process (Feed processing, dismantling, ante/post DB analysis and interpretation)
Java 8 capabilities were used to a high extent to archive this. 
Java 9 could have been used (Reactive features) for this type of problem, but I have never before worked with Java 9, so Java 8 was my choice. 

For example, [ROME framework](https://rometools.jira.com/wiki/spaces/ROME/overview) was used to read feeds, but solution is modular in that sense that it's easily adoptable to using some other method or framework just for feeds 'parsing'.

### Analysis

Since analysis of human/natural language in sense of finding similarities of texts based on some key, or most frequent words, is generally very, very complex problem, not solvable in the given time. 
And since in memory DB is proposed as the part of the solution, analysis algorithm implemented is very simple. 

Following assumptions are made:

- only english language feeds are processed
- analysis will be based only on comparing feed titles
- analysis will not enter semantics of the words (only words, not Synts or other variations of the words will be used) nor their relation

The main idea was to find 'descriptors' (words) that titles of feed entries have in common.
'Words' here are latterly that - words (not tokens, not synts). 

To remove clutter form most frequent words, additional solution was used: [cue.language](https://github.com/jdf/cue.language)
Though this solution is embedded into project, I do not take any credit for it - it was just the most simplest way to embed it into the project
(it could have been made into Maven dependant module, but i chose easier solution to spare some time)

Once we have descriptors that hold relations to the items they refer to, these descriptors are stored into DB.
Later on, analysis of items related to descriptors is made. 
Yes, storing descriptors seems like a bit of overkill. And yes, the exercise hinted that analysis should be done prior to storing to DB. 
But, since it's not clearly specified what similarity of items exactly is, this solution makes the reader ('second' end point) the interpreter of the results.  

As for the implementation of the interpreter, again, most simplest solution is provided:
- without entering the domain of 'potentially similar' item relations (which, can't be done without entering the domain of word semantics) - similar items are those that have most relations with other items (not necessarily same relations, nor same relations with same items)

That is why the algorithm will not always give best results (and will occasionally recognise completely unrelated items as most viral). But without semantics context and specified relations between the words, specified weights of those relations, doing more is not much more possible.


### Data Access and Service layers

Relational DB consist of very few tables, trying to be very light on the relations between entities. 
Data access layer is generally made very light, with less as possible insertions and reads, and is mostly used as permanent buffer between two endpoints calls. 

Service layer takes on itself the logic of switching and chaining operations.

