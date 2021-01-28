import com.ibm.dbb.build.*

hlq        = "IBMUSER.BUILD"
sourceDir  = "/S0W1/etc/dbb/jenkins/workspace/HelloGitFinal/HelloWorldTest"
compilerDS = "IGY630.SIGYCOMP"

println("******************************************")
println("Creating Mainframe PDS ${hlq}.COBOL")
println("******************************************")
CreatePDS createPDSCmd = new CreatePDS();
createPDSCmd.setDataset("${hlq}.COBOL");
createPDSCmd.setOptions("tracks space(1,1) lrecl(80) dsorg(PO) recfm(F,B) dsntype(library)");
createPDSCmd.create();

println("******************************************")
println("Creating Mainframe PDS ${hlq}.OBJ")
println("******************************************")
createPDSCmd.setDataset("${hlq}.OBJ");
createPDSCmd.setOptions("tracks space(1,1) lrecl(80) dsorg(PO) recfm(F,B) dsntype(library)");
createPDSCmd.create();

println("******************************************")
println("Copying ${sourceDir}/helloworld.cbl")
println("                                TO")
println("        ${hlq}.COBOL(HELLO)")
println("******************************************")
def copy = new CopyToPDS().file(new File("${sourceDir}/helloworld.cbl")).dataset("${hlq}.COBOL").member("HELLO")
copy.execute()

println("******************************************")
println("Compiling ${hlq}.COBOL(HELLO)")
println("******************************************")
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
        println("******************************************")
        println("           Compile failed!  RC=$rc")
        println("******************************************")
else
        println("******************************************")
        println("        Compile successful!  RC=$rc")
        println("******************************************")
