%%
% inputData = [rand(10,1) 10*rand(10,1)-5];
% outputData = rand(10,1);
% 
% opt = genfisOptions('GridPartition');
% opt.NumMembershipFunctions = [3 5];
% opt.InputMembershipFunctionType = ["trimf" "trimf"];
% fis = genfis(inputData,outputData,opt);
% 
% [x,mf] = plotmf(fis,'input',1);
% subplot(2,1,1)
% plot(x,mf)
% xlabel('input 1 (gaussmf)')

%%

x = (0:0.1:10)';
y = sin(2*x)./exp(x/5);

options.NumMembershipFunctions = 5;
fisin = genfis(x,y,options)



