'Option Explicit

set args = Wscript.Arguments
arg1 = args.Item(0)

'arg2 = args.Item(1)
msgbox arg1


'msgbox arg1 & " " & arg2
        
    strHTML = "<html>" & _
               "<body>" & _
               "My <b><i>HTML</i></b> message text!" & _
               "</body>" & _
               "</html>"
   ' blnSuccessful = FnSafeSendEmail("write2shubhank@gmail.com", "My Message Subject", strHTML,"C:\DocGPS_Automation\DocGPSResult_Jul 1, 2011 11_31_07 'PM.txt;C:\DocGPS_Automation\DocGPSResult_Jul 2, 2011 1_43_18 PM.txt","","")    

   blnSuccessful = FnSafeSendEmail("write2shubhank@gmail.com", "My Message Subject", strHTML,arg1,"","") 

    'A more complex example...
    'blnSuccessful = FnSafeSendEmail( _
'                        "myemailaddress@domain.com; recipient2@domain.com", _
 '                       "My Message Subject", _
  '                      strHTML, _
   '                     "C:\MyAttachFile1.txt; C:\MyAttachFile2.txt", _
    '                    "cc_recipient@domain.com", _
     '                   "bcc_recipient@domain.com")

    If blnSuccessful Then
    
        MsgBox "E-mail message sent successfully!"
        
    Else
    
        MsgBox "Failed to send e-mail!"
    
    End If




'This is the procedure that calls the exposed Outlook VBA function...
Public Function FnSafeSendEmail(strTo,strSubject,strMessageBody,strAttachmentPaths,strCC,strBCC )

'    Dim objOutlook As Object ' Note: Must be late-binding.
 '   Dim objNameSpace As Object
   ' Dim objExplorer As Object
    'Dim blnSuccessful As Boolean
    'Dim blnNewInstance As Boolean
    
    'Is an instance of Outlook already open that we can bind to?
    On Error Resume Next
    Set objOutlook = GetObject(, "Outlook.Application")
    On Error GoTo 0
    
    If objOutlook Is Nothing Then
    
        'Outlook isn't already running - create a new instance...
        Set objOutlook = CreateObject("Outlook.Application")
        blnNewInstance = True
        'We need to instantiate the Visual Basic environment... (messy)
        Set objNameSpace = objOutlook.GetNamespace("MAPI")
        Set objExplorer = objOutlook.Explorers.Add(objNameSpace.Folders(1), 0)
        objExplorer.CommandBars.FindControl(, 1695).Execute
                
        objExplorer.Close
                
        Set objNameSpace = Nothing
        Set objExplorer = Nothing
        
    End If
	'msgbox strTo
	'strTo=trim(cstr(strTo))
	'strTo= cstr("pankaj_behl@uhc.com")
    bln= objOutlook.FnSendMailSafe(strTo,strCC,strBCC,strSubject,strMessageBody,strAttachmentPaths)
                                
    If blnNewInstance = True Then objOutlook.Quit
    Set objOutlook = Nothing
    
    FnSafeSendEmail = bln
    
End Function
