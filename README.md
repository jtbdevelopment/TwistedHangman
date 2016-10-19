Twisted Hangman
===============

Offering online variations of the classic game, including live or turn-based pacing against the computer or vs as many online competitors as you wish.

##Tech Stack:##
The default stack is:

*  Mongo DB data store (run against 2.4 and 2.6 versions)
*  1.8 JVM-based (most code is groovy 2.3.x) backend
*  REST provided by Jersey/Jackson module
*  Bootstrap/Angular/SASS/Compass front-end
*  Spring, Spring-Data, slf4j, logback are the other main components

There is also the intention to use protractor to add full e2e testing for front-end.  TODO.

## Dev Notes ##
The primary build mechanism is maven.  However, for the front-end we use front-end-maven plugin to wrap yeoman, grunt, and karma so normal Javascript tools can be built.

Please see front-end notes for first time build of

## MongoDB setup ##
The default config presumes a local mongo instance WITH AUTHENTICATION enabled.  The presumption is a db/login/password of 'twistedhangman' all exist.

After running mongo and confirming login details you will want to run a few one time utilities.

*  SystemIdMaker - will generate system user
*  ManualPlayerMaker - will make a few manual players to use so you can develop locally without facebook.
*  WheelOfFortuneSolutionsLoader - will parse a Wheel of Fortune puzzle site to create ~10k pre-canned games to use.

## Backend  ##
The primary DAO and game logic is in the game sub-module.  Unit tests only.

The rest module contains the mapping and exception handlers for the REST interface.  Unit tests only.

integration-tests module runs full-stack tests.  Currently these are back-end only (no e2e for front-end yet).

You can run GrizzlyServerBuilder which will kick off a running back end on port 9998 for you to interact with the back end or use for front-end.

##  Front-End  ##
The code is under the webUI module.  Maven is not necessary for dev-mode.  Maven wraps the result into a jar for later integration.

The front-end development presumes you have the following available:

*  node.js
*  npm
*  grunt (and command line)
*  karma (and command line)
*  yeoman
*  ruby/sass/compass for sass builds
*  Tomcat

The front end development module was kick-started with yeoman.

The front end depends on a backend to be up and running to work.  Grunt proxy has been configured to point api calls to localhost 9998.  You can use the JettyServer command to run an embedded jetty backend for testing.

If your webUI build fails it may be for this reason and you will need to do a one time manual bower build.

From a command prompt you should be able to run:

npm install:  to install npm and bower packages.

grunt serve: to kick off local instance proxying api calls to backend

karma start test/karma.conf.js for unit testing.  Karma unit tests also run with code coverage report (see coverage dir)