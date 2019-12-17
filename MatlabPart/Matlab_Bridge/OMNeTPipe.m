classdef OMNeTPipe
    %OMNeTPipe A connection between MATLAB and OMNeT++
    
    properties
        % Sever Connection
        serverConn
        
        % Message primatives
        msgBegin
        msgFieldSep
        msgIntSep
        msgEnd
        
        % Type data
        typeStr
        typeInt
        typeFloat
        typeDouble
    end
    
    methods
        % Constructor
        function obj = OMNeTPipe( host, port )
            % Set up connection
            obj.serverConn = tcpip(host, port, 'NetworkRole', 'server');
            set(obj.serverConn, 'InputBufferSize', 4096);
            set(obj.serverConn, 'Timeout', 20);
            fopen(obj.serverConn);
            
            % Send synchronizing byte
            fwrite(obj.serverConn, '0');
            
            % Set up message primatives
            obj.msgBegin = char(1);
            obj.msgFieldSep = char(24);
            obj.msgIntSep = char(25);
            obj.msgEnd = char(4);
            
            % Set up type primatives
            obj.typeStr = 3;
            obj.typeInt = 0;
            obj.typeFloat = 1;
            obj.typeDouble = 2;
        end
        
        % formPk
        % @brief forms a pk from the provided arguments
        % 
        % @param obj         the pipe to use
        % @param header      the header for the packet
        % @param varargin    the packet contents
        % @return            the packet string
        function pk = formPk( obj, header, varargin )
            pk = '';

            % Check length
            if (mod(length(varargin{1}), 3) ~= 0)
                return;
            end
            
            % Process args
            % Insert header
            pk = strcat(obj.msgBegin, header);

            % Get args into var list
            for i = 1:3:length(varargin{1})
                val{i} = varargin{1}{i};
                valType(i) = varargin{1}{i + 1};
                valName{i} = varargin{1}{i + 2};
            end

            for i = 1:3:length(varargin{1})
                % Convert non-string args to be strings
                if (valType(i) ~= obj.typeStr)
                    val{i} = num2str(val{i});
                end

                % Form this packet field
                pk = strcat(pk, obj.msgFieldSep, valName{i}, ...
                    obj.msgIntSep, val(i), obj.msgIntSep, ...
                    num2str(valType(i)));
            end

            pk = strcat(pk, obj.msgEnd);
            pk = char(pk);
        end
    
        % parsePk
        % @brief parses the given packet string
        % 
        % @param obj         the pipes to use
        % @param pk          the packet string
        % @return            the packet in a cell matrix and the header
        function [h, v] = parsePk( obj, pk )
            val = '';
            valName = '';
            valType = '';
            onHeader = 1;
            onVal = 0;
            onType = 0;
            onName = 1;
            
            v = containers.Map;
            h = '';
            % Process
            for i = 2:length(pk)
                if ((double(pk(i)) == double(obj.msgFieldSep)) || ...
                        (double(pk(i)) == double(obj.msgEnd)))
                    % If its the header, set it so, else, parse and add to
                    % the dictionary
                    if (onHeader == 1)
                        h = valName;
                        onHeader = 0;
                        valName = '';
                    else
                        % Convert to int/double
                        if (valType ~= obj.typeStr)
                            val = str2num(val);
                        end
                        
                        v(valName) = val;
                        
                        % Reset state
                        onVal = 0;
                        onType = 0;
                        onName = 1;
                        val = '';
                        valType = '';
                        valName = '';
                    end
                elseif (double(pk(i)) == double(obj.msgIntSep))
                    % Switch types if on new packet section
                    if (onVal == 1)
                        onVal = 0;
                        onType = 1;
                    elseif (onName == 1)
                        onName = 0;
                        onVal = 1;
                    end
                else
                    % Put char in proper container
                    if (onVal == 1)
                        val = strcat(val, pk(i));
                    elseif (onName == 1)
                        valName = strcat(valName, pk(i));
                    elseif (onType == 1)
                        valType = str2double(pk(i));
                    end
                end
            end
                
        end
        
        % sendPk
        % @brief sends a packet to the remote side
        %
        % @param obj         the pipe to use
        % @param header      the header for the packet
        % @param varargin    the packet data
        % @return            void
        function sendPk( obj, header, varargin )
            pk = formPk(obj, header, varargin);
            fwrite(obj.serverConn, pk);
        end
        
        % recvPk
        % @brief sends a packet to the remote side
        %
        % @param obj         the pipe to use
        % @return            the packet in a cell matrix and the header
        function [h, v] = recvPk( obj )
            pkChar = '';
            pk = '';
            while (strcmp(char(pkChar), obj.msgEnd) == 0)
                pkChar = fread(obj.serverConn, 1);
                pk = strcat(pk, char(pkChar));
            end
            
            [h, v] = parsePk(obj, pk);           
        end
    
    end
    
end

