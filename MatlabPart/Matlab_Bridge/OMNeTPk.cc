/* OMNeTPk class
 *
 *
 * Michael Rosen
 * mrrosen
 * 3-8-2014
 *
 */
 
#include "OMNeTPk.h"
 
/* Constructor */
OMNeTPk::OMNeTPk(char* h) {
	size = 0;
	
	header = static_cast<char*>(malloc(strlen(h)));
	strcpy(header, h);
}

/* Deconstructor */
OMNeTPk::~OMNeTPk(void) {
	free(header);
}

/* addVal
 * @brief adds a new variable to the packet
 * 
 * @param name the name of the new variable
 * @param val the value of the new variable
 * @param type the type of the new variable
 * @return void
 */
void OMNeTPk::addVal(char* name, void* val, int type) {
	/* Deep copy the name */
	char* deep_val;
	char* deep_name = static_cast<char*>(malloc(strlen(name)));
	strcpy(deep_name, name);
	
	/* Deep copy the value if its a string */
	switch (type) {
	case TYPE_DOUBLE:
		deep_val = static_cast<char*>(malloc(sizeof(double)));
		break;
	case TYPE_STR:
		deep_val = static_cast<char*>(malloc(strlen(static_cast<char*>(val))));
		strcpy(deep_val, static_cast<char*>(val));
		break;
	default:
		deep_val = static_cast<char*>(val);
		break;
	}
	
	/* Add these to packet */
	size++;
	names.push_back(deep_name);
	vals.push_back(deep_val);
	types.push_back(type);
	
	return;
}

/* getName
 * @brief gets the name of the indexth variable in the packet
 *
 * @param index the number of the variable
 * @return the name of the variable
 */
char* OMNeTPk::getName(int index) {
	return names[index];
}

/* getVal
 * @brief gets the value of the variable
 *
 * @param name the name of the variable
 * @return the value of the variable
 */
void* OMNeTPk::getVal(char* name) {
	unsigned int i;
	i = find(names.begin(), names.end(), name) - names.begin();
	if (i == names.size()) {
		/* ERROR */
		return NULL;
	}
	return vals[i];
}

void* OMNeTPk::getVal(int i) {
    return vals[i];
}

/* getType
 * @brief gets the type of the variable
 *
 * @param name the name of the variable
 * @return the type of the variable
 */
int OMNeTPk::getType(char* name) {
	unsigned int i;
	i = find(names.begin(), names.end(), name) - names.begin();
	if (i == names.size()) {
		/* ERROR */
		return -1;
	}
	return types[i];
}

/* getHeader
 * @brief gets the header of the packet
 *
 * @return the header
 */
char* OMNeTPk::getHeader(void) {
	return header;
}

/* getSize
 * @brief gets the size of the packet
 *
 * @return the size
 */
int OMNeTPk::getSize(void) {
	return size;
}
