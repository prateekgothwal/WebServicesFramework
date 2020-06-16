import com.thinking.machines.annotations.*;
import com.thinking.machines.students.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
public class PDFGenerator
{
public static ArrayList<String> files=new ArrayList<String>();
public static String path="";


static void RecursivePrint(File[] arr,int index,int level) 
{ 
if(index == arr.length) return;
if(arr[index].isFile())
{ 
if(arr[index].getName().endsWith(".class"))
{
if(!arr[index].getName().endsWith("Path.class") && !arr[index].getName().endsWith("ClassType.class") && !arr[index].getName().endsWith("Secure.class") && !arr[index].getName().endsWith("Forward.class") && !arr[index].getName().endsWith("RequestData.class") && !arr[index].getName().endsWith("ResponseType.class"))
{
String k=path.substring(54);
String l=k.replace("\\",".")+"."+arr[index].getName().replace(".class","");
files.add(new String(l)); 
}
}
}
else if(arr[index].isDirectory()) 
{
path="";
path+=arr[index];
RecursivePrint(arr[index].listFiles(), 0, level + 1); 
}
RecursivePrint(arr,++index, level);
}

public static void main(String gg[])
{

try
{
String maindirpath=gg[0];
File maindir = new File(maindirpath);
File arr[] = maindir.listFiles();
RecursivePrint(arr,0,0);
ArrayList<String> list=new ArrayList<>();
com.itextpdf.text.Document servicesDocument=new com.itextpdf.text.Document();
com.itextpdf.text.pdf.PdfWriter servicesPdfWriter=com.itextpdf.text.pdf.PdfWriter.getInstance(servicesDocument,new FileOutputStream(gg[1]));
com.itextpdf.text.Document errorsDocument=new com.itextpdf.text.Document();
com.itextpdf.text.pdf.PdfWriter errorsPdfWriter=com.itextpdf.text.pdf.PdfWriter.getInstance(errorsDocument,new FileOutputStream(gg[2]));
servicesDocument.open();
errorsDocument.open();
int pageNumber=0;
int pageSize=30;
boolean newPage=true;
com.itextpdf.text.Paragraph paragraph;
com.itextpdf.text.Font classFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,14,com.itextpdf.text.Font.BOLD);
com.itextpdf.text.Font methodFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,14,com.itextpdf.text.Font.NORMAL);
com.itextpdf.text.Font parameterFont=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,12,com.itextpdf.text.Font.NORMAL);
paragraph=new com.itextpdf.text.Paragraph("SERVICES",new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,16,com.itextpdf.text.Font.BOLD));
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
servicesDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("ERRORS",new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,16,com.itextpdf.text.Font.BOLD));
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
errorsDocument.add(paragraph);
for(String s: files)
{
Class c=Class.forName(s);
Annotation [] annotations=c.getAnnotations();
for(Annotation annotation: annotations)
{
if(annotation instanceof Path)
{
Path myAnno=(Path)annotation;
Method [] methods=c.getMethods();
int i=0;
int j=0;
for(Method mm:methods)
{
if(mm.getAnnotation(Path.class)!=null)
{
if(list.contains(myAnno.value()+mm.getAnnotation(Path.class).value()))
{
if(i==0)
{
paragraph=new com.itextpdf.text.Paragraph("Class Name : "+c.getName(),classFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Class Annotation : "+myAnno.value(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
i=1;
}
paragraph=new com.itextpdf.text.Paragraph("Method Name : "+mm.getName()+"   ;    Annotation Name : Path",methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Method Annotation : "+mm.getAnnotation(Path.class).value()+"   ;    Return Type : "+mm.getReturnType(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);

Annotation[][] Arrayannotations=mm.getParameterAnnotations();
int z=1;
if(Arrayannotations.length!=0)
{
for(Annotation [] annotationRow: Arrayannotations)
{
for(Annotation anno:annotationRow)
{
if(anno instanceof ClassType)
{
ClassType classType=(ClassType)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : ClassType  ;  Parameter Annotation Value : "+classType.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
}
if(anno instanceof RequestData)
{
RequestData requestData=(RequestData)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : Request Data  ;  Parameter Annotation Value : "+requestData.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
}
}
}
}
}
else
{
if(j==0)
{
paragraph=new com.itextpdf.text.Paragraph("Class Name : "+c.getName(),classFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Class Annotation : "+myAnno.value(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
j=1;
}
paragraph=new com.itextpdf.text.Paragraph("Method Name : "+mm.getName()+"   ;    Annotation Name : Path",methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Method Annotation : "+mm.getAnnotation(Path.class).value()+"   ;    Return Type : "+mm.getReturnType(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);

Annotation[][] Arrayannotations=mm.getParameterAnnotations();
int z=1;
if(Arrayannotations.length!=0)
{
for(Annotation [] annotationRow: Arrayannotations)
{
for(Annotation anno:annotationRow)
{
if(anno instanceof ClassType)
{
ClassType classType=(ClassType)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : ClassType  ;  Parameter Annotation Value : "+classType.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
}
if(anno instanceof RequestData)
{
RequestData requestData=(RequestData)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : Request Data  ;  Parameter Annotation Value : "+requestData.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
}
}
}
}
list.add(myAnno.value()+mm.getAnnotation(Path.class).value());
}
}


if(mm.getAnnotation(Forward.class)!=null)
{
if(list.contains(myAnno.value()+mm.getAnnotation(Forward.class).value()))
{
if(i==0)
{
paragraph=new com.itextpdf.text.Paragraph("Class Name : "+c.getName(),classFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Class Annotation : "+myAnno.value(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
i=1;
}
paragraph=new com.itextpdf.text.Paragraph("Method Name : "+mm.getName()+"   ;    Annotation Name : Forward",methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Method Annotation : "+mm.getAnnotation(Forward.class).value()+"   ;    Return Type : "+mm.getReturnType(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);

Annotation[][] Arrayannotations=mm.getParameterAnnotations();
int z=1;
if(Arrayannotations.length!=0)
{
for(Annotation [] annotationRow: Arrayannotations)
{
for(Annotation anno:annotationRow)
{
if(anno instanceof ClassType)
{
ClassType classType=(ClassType)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : ClassType  ;  Parameter Annotation Value : "+classType.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
}
if(anno instanceof RequestData)
{
RequestData requestData=(RequestData)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : Request Data  ;  Parameter Annotation Value : "+requestData.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
}
}
}
}
}
else
{
if(j==0)
{
paragraph=new com.itextpdf.text.Paragraph("Class Name : "+c.getName(),classFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Class Annotation : "+myAnno.value(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
j=1;
}
paragraph=new com.itextpdf.text.Paragraph("Method Name : "+mm.getName()+"   ;    Annotation Name : Forward",methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Method Annotation : "+mm.getAnnotation(Forward.class).value()+"   ;    Return Type : "+mm.getReturnType(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);

Annotation[][] Arrayannotations=mm.getParameterAnnotations();
int z=1;
if(Arrayannotations.length!=0)
{
for(Annotation [] annotationRow: Arrayannotations)
{
for(Annotation anno:annotationRow)
{
if(anno instanceof ClassType)
{
ClassType classType=(ClassType)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : ClassType  ;  Parameter Annotation Value : "+classType.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
}
if(anno instanceof RequestData)
{
RequestData requestData=(RequestData)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : Request Data  ;  Parameter Annotation Value : "+requestData.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
}
}
}
}
list.add(myAnno.value()+mm.getAnnotation(Forward.class).value());
}
}


if(mm.getAnnotation(ResponseType.class)!=null)
{
if(list.contains(myAnno.value()+mm.getAnnotation(ResponseType.class).value()))
{
if(i==0)
{
paragraph=new com.itextpdf.text.Paragraph("Class Name : "+c.getName(),classFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Class Annotation : "+myAnno.value(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
i=1;
}
paragraph=new com.itextpdf.text.Paragraph("Method Name : "+mm.getName()+"   ;    Annotation Name : Path",methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Method Annotation : "+mm.getAnnotation(ResponseType.class).value()+"   ;    Return Type : "+mm.getReturnType(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);

Annotation[][] Arrayannotations=mm.getParameterAnnotations();
int z=1;
if(Arrayannotations.length!=0)
{
for(Annotation [] annotationRow: Arrayannotations)
{
for(Annotation anno:annotationRow)
{
if(anno instanceof ClassType)
{
ClassType classType=(ClassType)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : ClassType  ;  Parameter Annotation Value : "+classType.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
}
if(anno instanceof RequestData)
{
RequestData requestData=(RequestData)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : Request Data  ;  Parameter Annotation Value : "+requestData.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
errorsDocument.add(paragraph);
}
}
}
}
}
else
{
if(j==0)
{
paragraph=new com.itextpdf.text.Paragraph("Class Name : "+c.getName(),classFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Class Annotation : "+myAnno.value(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
j=1;
}
paragraph=new com.itextpdf.text.Paragraph("Method Name : "+mm.getName()+"   ;    Annotation Name : Path",methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
paragraph=new com.itextpdf.text.Paragraph("Method Annotation : "+mm.getAnnotation(Path.class).value()+"   ;    Return Type : "+mm.getReturnType(),methodFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);

Annotation[][] Arrayannotations=mm.getParameterAnnotations();
int z=1;
if(Arrayannotations.length!=0)
{
for(Annotation [] annotationRow: Arrayannotations)
{
for(Annotation anno:annotationRow)
{
if(anno instanceof ClassType)
{
ClassType classType=(ClassType)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : ClassType  ;  Parameter Annotation Value : "+classType.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
}
if(anno instanceof RequestData)
{
RequestData requestData=(RequestData)anno;
paragraph=new com.itextpdf.text.Paragraph("Parameter Annotation : Request Data  ;  Parameter Annotation Value : "+requestData.value(),parameterFont);
paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
servicesDocument.add(paragraph);
}
}
}
}
list.add(myAnno.value()+mm.getAnnotation(ResponseType.class).value());
}
}

}
}
}
paragraph=new com.itextpdf.text.Paragraph("\n");
servicesDocument.add(paragraph);
}
servicesDocument.close();
errorsDocument.close();
}
catch(ClassNotFoundException cnfe)
{
cnfe.printStackTrace();
}
catch(Exception e)
{
e.printStackTrace();
}
}
}