import com.ibm.dbb.build.*

hlq        = "IBMUSER.BUILD"
sourceDir  = "/u/ibmuser/mainframe-devops-helloworld/HelloWorldTest"
compilerDS = "IGY630.SIGYCOMP"

println("Creating ${hlq}.COBOL. . .")
CreatePDS createPDSCmd = new CreatePDS();
createPDSCmd.setDataset("${hlq}.COBOL");
createPDSCmd.setOptions("tracks space(1,1) lrecl(80) dsorg(PO) recfm(F,B) dsntype(library)");
createPDSCmd.create();

println("Creating ${hlq}.OBJ. . .")
createPDSCmd.setDataset("${hlq}.OBJ");
createPDSCmd.setOptions("tracks space(1,1) lrecl(80) dsorg(PO) recfm(F,B) dsntype(library)");
createPDSCmd.create();

println("Copying ${sourceDir}/helloworld.cbl to ${hlq}.COBOL(HELLO) . . .")
def copy = new CopyToPDS().file(new File("${sourceDir}/helloworld.cbl")).dataset("${hlq}.COBOL").member("HELLO")
copy.execute()


println("Compiling ${hlq}.COBOL(HELLO). . .")
def compile = new MVSExec().pgm("IGYCRCTL").parm("LIB")
compile.dd(new DDStatement().name("SYSIN").dsn("${hlq}.COBOL(HELLO)").options("shr"))
compile.dd(new DDStatement().name("SYSLIN").dsn("${hlq}.OBJ(HELLO)").options("shr"))

(1..17).toList().each { num ->
	compile.dd(new DDStatement().name("SYSUT$num").options("cyl space(5,5) unit(vio) new"))
	   }

compile.dd(new DDStatement().name("SYSMDECK").options("cyl space(5,5) unit(vio) new"))
compile.dd(new DDStatement().name("TASKLIB").dsn("${compilerDS}").options("shr"))
compile.dd(new DDStatement().name("SYSPRINT").options("cyl space(5,5) unit(vio)  new"))
compile.copy(new CopyToHFS().ddName("SYSPRINT").file(new File("${sourceDir}/worklog/helloworld.log")))
def rc = compile.execute()

if (rc > 4)
    println("Compile failed!  RC=$rc")
else
    println("Compile successful!  RC=$rc")
