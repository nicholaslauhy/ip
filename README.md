# Lumi project template

This was made based on a project template for a greenfield Java project. It's inspired by my favourite character in Brawl Stars, Lumi. Lumi has a BIG attitude, but he will surely help you get the job done! 
Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 17, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
2. Open the project into Intellij as follows:
   a. Click `Open`.
   b. Select the project directory, and click `OK`.
   c. If there are any further prompts, accept the defaults.
3. Configure the project to use **JDK 17** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
4. After that, locate the `src/main/java/Lumi.java` file, right-click it, and choose `Run Lumi.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
   I am your Task Manager! What's your name?
   ```
   whereby if you key in your name, you will get the following:
   ```
   Hello <name in capital letters>, I am
    _      _   _   __  __   ___
   | |    | | | | |  \/  | |_ _|
   | |    | | | | | |\/| |  | |
   | |___ | |_| | | |  | |  | |
   |_____| \___/  |_|  |_| |___|
   
   I am LUMIIII, ready when you are! --> randomised introduction message from Lumi
   ____________________________________________________________
   Tasks for <name>:
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
