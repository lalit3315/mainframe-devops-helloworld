       IDENTIFICATION DIVISION.
       PROGRAM-ID.     HELLO.
      *First program by lalit kapila to test
      *DBB working
      *compile COBOL program using DBB, Git and jenkins
       PROCEDURE DIVISION.
           DISPLAY "Hello world!".
           DISPLAY "Lalit has done it".
      *CH105 Changes Start
           DISPLAY "Demo Line Number# 1 Added".
           DISPLAY "Demo Line Number# 2 Added".
           DISPLAY "Demo Line Number# 3 Added".
           DISPLAY "Demo Line Number# 4 Added".
           DISPLAY "Demo Line Number# 5 Added".
      *CH105 Changes End

      *CH101 below line 3 commented
      *    DISPLAY "Line Number# 3 Added".
           DISPLAY "Line Number# 4 Added".
           DISPLAY "Test Rocket Git ASCII to EBCDIC".
      *CH102 comment live demo v0.1
           DISPLAY "Test run before DEMO on 18 May".
           STOP RUN.