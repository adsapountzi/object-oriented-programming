//Author: Sapountzi Athanasia Despoina 02624
#include "AVL.hpp"

using namespace std;

AVL::Node::Node(const string& e, Node *parent, Node *left, Node *right){
	this->element = e;
	this->parent = parent;
	this->left = left;
	this->right = right;
	//this->height=1;
	this->height=1;
}

AVL::Node* AVL::Node::getParent() const{
	return this->parent;
}

AVL::Node* AVL::Node::getLeft() const{
	return this->left;
}

AVL::Node* AVL::Node::getRight() const{
	return this->right;
}

string  AVL::Node::getElement() const{
	return this->element;
}

//????????????????????
int AVL::Node::getHeight() const{
	return this->height;
	 // if(!(this == NULL)){
  //       return height;
  //   }

  //   return 0;
}

void AVL::Node::setLeft(AVL::Node *ptr){
	this->left = ptr;
}

void AVL::Node::setRight(AVL::Node *ptr){
	this->right = ptr;
}

void AVL::Node::setParent(AVL::Node *ptr){
	this->parent=ptr;
}

void AVL::Node::setElement(string e) {
    this->element = e;
}


bool AVL::Node::isLeft() const{
	if(this->getParent() == nullptr) //???? NULL || nullptr?
		return false;
	if(this->getParent()->getLeft() == this)
		return true;

	return false;
}

bool AVL::Node::isRight() const{
	if(this->getParent() == nullptr) //???? NULL || nullptr?
		return false;
	if(this->getParent()->getRight() == this)
		return true;

	return false;
}

int AVL::Node::rightChildHeight() const {
	return this->getRight()->getHeight();
}

int AVL::Node::leftChildHeight() const {
	return this->getLeft()->getHeight();
}


//update height and return max 
int AVL::Node::updateHeight(){
	if(this->getLeft() == nullptr && this->getRight()==nullptr){ //if it is a leaf node
		this->height = 1;	
	}
	else if(this->getLeft() == nullptr){
		// height of the right child gets increased by 1
		this->height = (this->getRight()->getHeight()+1);	
	}
	else if(this->getRight() == nullptr){
		// height of the left child gets increased by 1
		this->height = (this->getLeft()->getHeight()+1);
	}
	else{
		this->height = (max((this->getLeft()->getHeight()), (this->getRight()->getHeight())) + 1);
	}

	return this->height;
}


//method that checks if node is balanced  ????do i need this operator???
bool AVL::Node::isBalanced(){
	//leaf
	if((this->getLeft() == nullptr) && (this->getRight() == nullptr))
		return true;	
	
	else if(this->getLeft() == nullptr){
			if(this->rightChildHeight() <= 1)
				return true;
			else
				return false;
	}
	else if(this->getRight() == nullptr) {
			if(this->leftChildHeight() <= 1)
				return true;
			else
				return false;
	}
	else if(abs(this->getLeft()->getHeight() - this->getRight()->getHeight()) <= 1)
			return true;
	else
		return false;
	 
}

								///---/// AVL ///---///

//default constructor 
AVL::AVL(){
	root = nullptr;
	size = 0;
}

// AVL copy constructor NOT DONE
AVL::AVL(AVL& cpAVL){
	root=nullptr;
	size=0;
	//
	for(Iterator it = cpAVL.begin(); it != cpAVL.end();it++) {
		this->add(*it);
	}
}

//method that checks if there is a node in the tree that contains string e. 
bool AVL::contains(string e){

	if(size != 0){
		Node* curr = root;
		int cmp;
		while(1){

			cmp = curr->getElement().compare(e);
			
			if(cmp == 0){ //found string!!
				return true;
			}
			else if (cmp < 0){
				if(curr->getRight() == nullptr)
					break;
				curr = curr->getRight();
			}
			else if(cmp > 0){
				if(curr->getLeft() == nullptr)
					break;
				curr = curr->getLeft();
			}
		}
	}
	
	return false;
}

//inspired by the book from the course domes dedomenwn 
AVL::Node *AVL::rebalanceSon(AVL::Node *v){
    if(v->getLeft() == nullptr)
        return v->getRight();
    
    else if(v->getRight() == nullptr)
        return v->getLeft();

    else if(v->getLeft()->getHeight() > v->getRight()->getHeight())
        return v->getLeft();

    else if(v->getLeft()->getHeight() < v->getRight()->getHeight())
        return v->getRight();

    else if(v->isLeft())
        return v->getLeft();
    
    return v->getRight();
}

void AVL::rebalance(AVL::Node *v){
    Node *u, *w;
    while(v != nullptr){
        v->updateHeight();
        if(!v->isBalanced()){
            w = rebalanceSon(v);
            u = rebalanceSon(w);

            v = reconstruct(v, w, u);

            v->getLeft()->updateHeight();
            v->getRight()->updateHeight();
            v->updateHeight();
        }
        v = v->getParent();
    }
}



AVL::Node *AVL::reconstruct(AVL::Node *v, AVL::Node *w, AVL::Node *u){
    if(w->isLeft() && u->isLeft()){ //right simple rotation
        if(v != root){
            if(v->isLeft()){
                v->getParent()->setLeft(w);
            }
            else{
                v->getParent()->setRight(w);
            }
            w->setParent(v->getParent());
        }
        v->setLeft(w->getRight());
        if(w->getRight() != nullptr){
            w->getRight()->setParent(v);
        }
        w->setRight(v);
        v->setParent(w);
        if(v == root){
            root = w;
            w->setParent(nullptr);
        }
        return w;
    }
    
    if(w->isRight() && u->isRight()){ //left simple rotation
        if(v != root){
            if(v->isRight()){
                v->getParent()->setRight(w);
            }
            else{
                v->getParent()->setLeft(w);
            }
            w->setParent(v->getParent());
        }
        v->setRight(w->getLeft());
        if(w->getLeft() != nullptr){
            w->getLeft()->setParent(v);
        }
        w->setLeft(v);
        v->setParent(w);
        if(v == root){
            root = w;
            w->setParent(nullptr);
        }
        return w;
    }
    
    if(u->isLeft()){ //double left rotation
        v->setRight(u->getLeft());
        if(u->getLeft() != nullptr){
            u->getLeft()->setParent(v);
        }
        w->setLeft(u->getRight());
        if(u->getRight() != nullptr){
            u->getRight()-> setParent(w);
        }
        if(v != root){
            if(v->isRight()){
                v->getParent()->setRight(u);
            }
            else{
                v->getParent()->setLeft(u);
            }
            u->setParent(v->getParent());
        }
        v->setParent(u);
        w->setParent(u);
        u->setLeft(v);
        u->setRight(w);
        if(v == root){
            root = u;
            u->setParent(nullptr);
        }
        return u;
    }
    else{ //double right rotation
        v->setLeft(u->getRight());
        if(u->getRight() != nullptr){
            u->getRight()->setParent(v);
        }
        w->setRight(u->getLeft());
        if(u->getLeft() != nullptr){
            u->getLeft()-> setParent(w);
        }
        if(v != root){
            if(v->isLeft()){
                v->getParent()->setLeft(u);
            }
            else{
                v->getParent()->setRight(u);
            }
            u->setParent(v->getParent());
        }
        v->setParent(u);
        w->setParent(u);
        u->setRight(v);
        u->setLeft(w);
        if(v == root){
            root = u;
            u->setParent(nullptr);
        }
        return u;
    }
}


bool AVL::add(string e){
	Node* currNode;
	Node* parentNode;

	currNode = root; 
 	
 	if(currNode == nullptr){
		Node* newNode = new Node(e, nullptr, nullptr, nullptr);
		root = newNode;
		size++;
		return true;
	}
	
	if(contains(e) == true) //string allready exists!
		return false;

	while(currNode != nullptr){
		if(currNode->getElement() > e){
			 parentNode = currNode;
             currNode = currNode->getLeft();
		}
		else if(currNode->getElement() < e){
			parentNode = currNode;
			currNode = currNode->getRight();
		}
	}

	Node* newNode = new Node( e, parentNode, nullptr, nullptr);
	
	if(newNode->getElement().compare(parentNode->getElement()) < 0) //?is it ok th cmp and <??
	   parentNode->setLeft(newNode);
        
	else // ????
		parentNode->setRight(newNode); 

	//update height after new node addition and check balance
	rebalance(newNode);

	//Increment size.
	size++;
	return true;
}


bool AVL::rmv(string e){

	Node *toBeRemoved;
	Node *temp;
	// int cmp;

	if(this->size == 0){
		//tree is empty
		return false;
	}
	toBeRemoved = root;

	//find node with element = e	
	while(1){

		if(toBeRemoved->getElement().compare(e) == 0){
			break; //found it!
		}
		else if(toBeRemoved->getElement().compare(e) > 0){
			if(toBeRemoved->getLeft() == nullptr){
				//reached end of left side!
				toBeRemoved = nullptr; //ret
				break;
			}
			toBeRemoved = toBeRemoved->getLeft();
		}
		else if(toBeRemoved->getElement().compare(e) < 0){
			if(toBeRemoved->getRight() == nullptr){
				//reached end of right side!
				toBeRemoved = nullptr; //ret
				break;
			}
			toBeRemoved = toBeRemoved->getRight();
		}
	}

	if(toBeRemoved == nullptr){
		//element doesn't exist in tree!!
		return false;
	}
	Node* currNode;
	if(toBeRemoved->getLeft() == NULL || toBeRemoved->getRight() == NULL){
        currNode = (toBeRemoved->getLeft() != NULL ? toBeRemoved->getLeft() : toBeRemoved->getRight() != NULL ? toBeRemoved->getRight() : toBeRemoved->getParent());
    }
    else{
        currNode = toBeRemoved->getParent();
        temp = toBeRemoved->getRight();
        while(temp->getLeft() != NULL){
            temp = temp->getLeft();
        }
        toBeRemoved->setElement(temp->getElement());
        temp->setElement(e);
        toBeRemoved = temp;
    }
    if(toBeRemoved == root){
        Node *node2;
        node2 = toBeRemoved->getLeft() != NULL ? toBeRemoved->getLeft() : toBeRemoved->getRight();
        root = node2;
        if(root != NULL){
            root->setParent(NULL);
			root->updateHeight();
        }
    }
    else{
        Node *parentOf, *notNullSon;
        parentOf = toBeRemoved->getParent();
        notNullSon = toBeRemoved->getLeft() != NULL ? toBeRemoved->getLeft() : toBeRemoved->getRight();
        if(toBeRemoved->isLeft()){
            parentOf->setLeft(notNullSon);
        }
        else{
            parentOf->setRight(notNullSon);
        }
        parentOf->updateHeight();
        if(notNullSon != NULL){
            notNullSon->setParent(parentOf);
			notNullSon->updateHeight();
        }
    }
    size--;
    rebalance(currNode);
    return true;
}
void AVL::printAvl(Node* node, string* helper){

    *helper = *helper + node->getElement() + " [shape=circle, color=black]" + "\n";
    if(node->getLeft() != NULL){
        *helper = *helper + node->getElement() + " -> " + node->getLeft()->getElement() + "\n";
        printAvl(node->getLeft(), helper);
    }
    
    if(node->getRight() != NULL){
        *helper = *helper + node->getElement() + " -> " + node->getRight()->getElement() + "\n";
        printAvl(node->getRight(), helper);
    }
}

void AVL::print2DotFile(char *filename){
    ofstream dotFile;
    string helper;
    
    dotFile.open(filename);
    dotFile << "AVL {" << endl;
    printAvl(root, &helper);
    dotFile << helper;
    dotFile << "}";
    dotFile.close();
}

//This function prints to the out stream the avl tree using pre order traversal
void AVL::pre_order(std::ostream& out)  const{	
	for(AVL::Iterator it = this->begin(); it != this->end(); ++it) {
		out << *it << " ";
	}
}

//This is the << overloaded method
std::ostream& operator<<(std::ostream& out, const AVL& tree){
	
	if(tree.root == nullptr){
		return out;
	}
	
	//Print the avl tree to the out stream by following the pre order rule
	tree.pre_order(out);
	out.flush(); //do i need it
	
	return out;
}


//This is the = overloaded method  mg
AVL& AVL::operator=(const AVL& avl){
	
	//Delete all the nodes from the left tree
	while(1){
		if(root==nullptr)
			break;
		for(Iterator it = this->begin(); it != this->end(); it++) {
			this->rmv(*it);
		}
	}
	
	//Copy all the nodes of the tree specified by the argument, to the left tree
	for(Iterator it = avl.begin(); it != avl.end(); it++) {
		this->add(*it);
	}
	
	return *this;
}

//the + overloaded method
AVL AVL::operator+(const AVL& avl){
	AVL avlNew;
	
	
	//copy nodes of the left tree to new avl 
	for(Iterator it = this->begin(); it != this->end();it++) {
		avlNew.add(*it);
	}
	
	//copy  nodes of the right tree to new avl
	for(Iterator it2 = avl.begin(); it2 != avl.end(); it2++) {
		avlNew.add(*it2);
	}
	return avlNew;
}


//This is the += overloaded method
AVL& AVL::operator+=(const AVL& avl){
	
	//Copy nodes of the tree avl to the new avl tree
	for(Iterator it = avl.begin(); it != avl.end(); it++) {
		add(*it);
	}
	return *this;
}


//the += overloaded method
AVL& AVL::operator+=(const string& e){
	
	if(contains(e) == true)
		return *this;
	else{
		add(e);
		return *this;
	}
	
}

//the -= overloaded method
AVL& AVL::operator-=(const string& e){
	
	if(contains(e)==false)
		return *this;
	//Remove the "e" node from the avl tree
	else{
		rmv(e);
		return *this;
	}
	
}

//the + overloaded method
AVL AVL::operator+(const string& e){
	
	//Create a new avl
	AVL newAVL(*this);
	//Add the node in the avl 
	newAVL.add(e);
	return newAVL;
}

//the - overloaded method
AVL AVL::operator-(const string& e){
	
	//new avl
	AVL newAVL(*this);
	//Remove node from the avl tree
	newAVL.rmv(e);
	return newAVL;
}


//ITERATOR
//default constructor of iterator
AVL::Iterator::Iterator(){
	
}

AVL::Iterator::Iterator(const AVL& avlTree){
	
	//Push into the iterator's stack the root node of the avl tree. 
	stack.push(avlTree.root);
	
}

string AVL::Iterator::operator*(){
//returns the top element from the iterator's stack
	return(stack.top()->getElement());
	
}

AVL::Iterator& AVL::Iterator::operator++(){
	
	Node* node;
	node = stack.top();
	stack.pop();

	if(!(stack.empty())){
		if(node->getRight() != nullptr){
			stack.push(node->getRight());
		}
		else if(node->getLeft()!=nullptr){
			stack.push(node->getLeft());
		}
	}
	else {
		stack.push(nullptr);
	}
	
	return *this;
	
}
//iterator's ++ overloaded method
AVL::Iterator AVL::Iterator::operator++(int a){
	
	Iterator newIterator;
	
	//Create a copy of the stack of the iterator object which called this method
	std::stack<AVL::Node*> copystack = stack;
	std::stack<AVL::Node*>* newstack = newIterator.getStackReference();
	
	//Copy all the stack from this iterator to the new iterator
	while(copystack.empty() == false){
		Node* node;
		node=copystack.top();
		copystack.pop();
		newstack->push(node);
		
	}
	
	//Dynamically increase the iterator position by loading new nodes into the iterator's stack
	Node* node;
	node=stack.top();
	stack.pop();
	
	if(node->getRight()!=nullptr){
		stack.push(node->getRight());
	}
	if(node->getLeft()!=nullptr){
		stack.push(node->getLeft());
	}
	if(stack.empty()){
		stack.push(nullptr);
	}
	
	//Return the new iterator
	return newIterator;
}


//the != overloaded method of the iterator
bool AVL::Iterator::operator!=(AVL::Iterator it){
	return !(this->operator==(it));
}

//the == overloaded method of the iterator
bool AVL::Iterator::operator==(AVL::Iterator it){
	if(stack.top() == it.stack.top())
		return true;
	else
		return false;
}

AVL::Iterator AVL::begin() const{
	
	Iterator newIt(*this);
	return newIt;
}

//Create a new iterator which contains only the nullptr inside his stack???
AVL::Iterator AVL::end() const{
	
	Iterator newIterator;
	std::stack<AVL::Node*>* stack = newIterator.getStackReference();
	stack->push(nullptr);
	
	return newIterator;
}

// AVL::Iterator AVL::end() const {
    
//     Iterator it(nullptr);

//     return (it);
// }


//This function returns the address of the iterator's stack
std::stack<AVL::Node*>* AVL::Iterator::getStackReference(){
	return &stack;
}

//This function returns an instance of the iterator's stack
std::stack<AVL::Node*> AVL::Iterator::getStack() const{
	return stack;
}
