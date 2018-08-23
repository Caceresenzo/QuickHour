

NET SESSION
IF %ERRORLEVEL% NEQ 0 GOTO ELEVATE
GOTO ADMINTASKS

:ELEVATE
CD /d %~dp0
MSHTA "javascript: var shell = new ActiveXObject('shell.application'); shell.ShellExecute('%~nx0', '', '', 'runas', 1);close();"
EXIT

:ADMINTASKS
%SystemRoot%\System32\reg.exe ADD HKEY_LOCAL_MACHINE\SOFTWARE\Classes\.qhr\shell\quickhour /ve /d "Quick Hour" /f
%SystemRoot%\System32\reg.exe ADD HKEY_LOCAL_MACHINE\SOFTWARE\Classes\.qhr\shell\quickhour /v Icon /t REG_SZ /d \"%~DP0\ressources\icon.ico\"
%SystemRoot%\System32\reg.exe ADD HKEY_LOCAL_MACHINE\SOFTWARE\Classes\.qhr\shell\quickhour\command /ve /d "\"%~DP0\start.cmd\" \"%%1\"" /f

%SystemRoot%\System32\reg.exe ADD HKEY_CLASSES_ROOT\SystemFileAssociations\.qhr\shell\quickhourdebug /ve /d "Quick Hour (DEBUG)" /f
%SystemRoot%\System32\reg.exe ADD HKEY_CLASSES_ROOT\SystemFileAssociations\.qhr\shell\quickhourdebug /v Icon /t REG_SZ /d \"%~DP0\ressources\icon.ico\"
%SystemRoot%\System32\reg.exe ADD HKEY_CLASSES_ROOT\SystemFileAssociations\.qhr\shell\quickhourdebug\command /ve /d "\"%~DP0\startpause.cmd\" \"%%1\"" /f

pause;