//asapountzi
#ifndef _GRAPH_HPP_ 
#define _GRAPH_HPP_

#include <list>
#include <ostream>
#include <vector>
#include <stack>
#include <queue>
#include <algorithm>
#include <limits>
#include <iostream>
#include <fstream>
#include <utility>

using namespace std;

template<typename T>
struct Edge {
  T from;
  T to;
  int dist;
  Edge(T f, T t, int d): from(f), to(t), dist(d) {
  }
  bool operator<(const Edge<T>& e) const{
        return dist < e.dist;
    };
    bool operator>(const Edge<T>& e) const{
        return dist > e.dist;
    }
    template<typename U>
    friend std::ostream& operator<<(std::ostream& out, const Edge<U>& e);
};

template<typename T>
std::ostream& operator<<(std::ostream& out, const Edge<T>& e) {
  out << e.from << " -- " << e.to << " (" << e.dist << ")";
  return out;
}

template <typename T>
class Graph {
  
public:
  Graph(bool isDirected = true){
      this->isDirected = isDirected;
  }
  
  
    vector<vector<T>> adjList;  //Adjacency list
    vector<Edge<T>> edges; // edges of the graph
   int size = 0;
   int sum;
   bool isDirected;
   bool contains(const T& info) {
        int i;

        for(i = 0; i < (int)adjList.size(); i++){
            if(adjList[i][0] == info){
                return true;
            }        
        }
        return false;
    }
      
  bool addVtx(const T& info){
    bool vertexExists = contains(info);
    vector<T> nVertex;


    if(vertexExists == false){ //  push vertex into the adjacency list
        nVertex.push_back(info);
        
        this->adjList.push_back(nVertex);
        
        size++;
        return true;
    }
    return false;//Adjacency list contains the vertex!
 }
      
  bool rmvVtx(const T& info){
    bool vertexExists = contains(info);
    int i, j;

    if(vertexExists == true) {//vertex exists in list
        for(i = 0; i < (int)adjList.size(); i++){
        // for(auto it = vec[i].begin();it != vec[i].end(); it++)
                
        if(adjList[i][0] == info){ 
            adjList.erase(adjList.begin() + i); //remove the row that is linked with that vertex!
            // adjList.erase(adjList[i]);
        }
        }
        for(i = 0; i < (int)adjList.size(); i++){
            for(j = 0; j < (int)adjList[i].size(); j++){
                if(adjList[i][j] == info){ //remove node that is linked with that vertex!
                    adjList[i].erase(adjList[i].begin() + j);
                    // adjList[i].erase(adjList[i][j]);
                }
            }
        }

    //     for(i = 0; i < (int)edges.size(); i++){
    //       if((edges[i].from == info) || (edges[i].to == info)) {
    //           edges.erase(edges.begin() + i);  
    //       }
    //     }
        for(auto it = edges.begin() ; it != edges.end(); ++it){
            if((it->from == info) || (it->to == info)) {
                    edges.erase(it);
                    it = edges.begin();
                    it--;
            }
        }
        
        size--;
        return true;
    }
  return false;
}


  bool addEdg(const T& from, const T& to, int distance){
    int i;

    if((contains(from) == true) && (contains(to) == true) && (edgeExists(from, to) == false)){
        for(i = 0; i < (int)adjList.size(); i++){  //same
                if(adjList[i][0] == from){
                    adjList[i].push_back(to);
                    Edge<T> temp(from, to, distance);
                    edges.push_back(temp);
                    
                    auto it = adjList[i].begin();
                    it++;
                        //chabnge this
                    
                    // After adding the edge, the adjacency list gets sorted so that vertices, added earlier in the graph, are in the right place.
                    sort(it, adjList[i].end(), [&](const T& first, const T& second) { return (findPlace(first))<(findPlace(second)); });
                }        
            }

            if(!isDirected){
                for(i = 0; i < (int)adjList.size(); i++){
                    if(adjList[i][0] == to){
                        adjList[i].push_back(from);

                        auto it = adjList[i].begin();
                        it++;
                        sort(it, adjList[i].end(), [&](const T& first, const T& second) { return (findPlace(first))<(findPlace(second)); });
                    }        
                }
            }
            return true;
        }
        return false;
  }
  bool rmvEdg(const T& from, const T& to){
   
    if((contains(from) == true) && (contains(to) == true) && (edgeExists(from, to) == true)){
      for(auto it = edges.begin() ; it != edges.end(); ++it){
        if(((it->from == from) && (it->to == to)) || (((it->from == to) && (it->to == from))&& (isDirected== false))){
          edges.erase(it);
          it = edges.begin();
          it--;
        }
      }
      return true;
    }
    return false;

  }
      
  std::list<T> dfs(const T& info) const{
        bool visited[size];
        list <T> result;
        
        for(int i = 0; i < size; i++){
            visited[i] = false; 
        }

        // Recursive function to help the dfs.
        dfsHelp(info, visited, result);

        return result;
    }
    
    void dfsHelp(T vertex, bool visited[], list <T> &result) const{
        int i;
        int pos = findPlace(vertex);
        visited[pos] = true;
        result.push_back(vertex);


        for(i = 0; i < (int)adjList[pos].size(); i++){
          if(visited[findPlace(adjList[pos][i])] == false){
            dfsHelp(adjList[pos][i], visited, result);
          }
        }
        
    }
     std::list<T> bfs(const T& info) const{
        bool visited[size];
        int i;
        queue <T> fifo;
        list <T> result;
        T curent = info;

        for(i = 0; i < size; i++){
            visited[i] = false;
        }
       
          visited[findPlace(info)] = true;
          
          fifo.push(info);
          result.push_back(info);
          

          while(fifo.size() > 0){  //same
              curent = fifo.front();
              fifo.pop();
              int pos = findPlace(curent);

              for(i = 0; i < (int)adjList[pos].size(); i++){
                  if(visited[findPlace(adjList[pos][i])] == false){
                      fifo.push(adjList[pos][i]);
                      result.push_back(adjList[pos][i]);
                      visited[findPlace(adjList[pos][i])] = true;
                  }
              }
          }
        
        return result;
    }
//   list<Edge<T>> mst();
   // Method implementing prims algorithm to find the minimum spanning tree.
    // It returns a list of edges which form the mst and it also calculates the minimum cost.
    std::list<Edge<T>> mst(){
        list<Edge<T>> result;
        list<Edge<T>> helper;
        bool visited[size];
        sum = 0;

        if(isDirected){
            return result;
        }

        for(int i = 0; i < size; i++){
            visited[i] = false;
        }

        mstHelp(adjList[0][0], result, visited, helper);

        for(auto it = result.begin(); it != result.end(); it++){
            if(findPlace(it->from) > findPlace(it->to)){
                T temp = it->from;
                it->from = it->to;
                it->to = temp;
            }
        }

        for(auto it = result.cbegin(); it != result.cend(); it++){
            sum = sum + it->dist;
        }

        return result;
    }
  
//   void print2DotFile(const char *filename) const;
bool print2DotFile(const char *filename) const{
        ofstream dotFile;

        dotFile.open(filename);

        if(size == 0){
            return false;
        }

        if(!isDirected){
            dotFile << "graph is not directeddd {\n";
        }
        else{
            dotFile << "graph is directeddd  {\n";
        }

        for(auto it = edges.begin(); it != edges.end(); it++){
            if(!isDirected){
                dotFile << it->from << " ----" << it->to << "weight is: " << it->dist << endl; 
            }
            else{
                dotFile << it->from << " ---> " << it->to << "weight is: " << it->dist << endl; 
            }
        }

        dotFile << "}\n";
        dotFile.close();
        
        return true;
    }
//   list<T> dijkstra(const T& from, const T& to);

    std::list<T> dijkstra(const T& from, const T& to){
        int dist[size]; // array with distances, initialized to INF.
        bool sptSet[size]; // array to store vertices with min distance found.
        list <T> result; // list with final distances.
        T previousVtx[size]; // array to store previous vertices in order to find the wanted path.
        int edgeDist;
        T vertex, vtxTemp;
        stack <T> helper;

        for(int j = 0; j < size; j++){
            dist[j] =  numeric_limits<int>::max();
            sptSet[j] = false;
        }
        
        dist[findPlace(from)] = 0; // first vertex dist = 0.

        for(int i = 0; i < size; i++){
            T temp = minDist(dist, sptSet);
            sptSet[findPlace(temp)] = true;


            for(auto it = adjList[findPlace(temp)].begin() + 1; it != adjList[findPlace(temp)].end(); ++it){
                edgeDist = computeDist(temp, *it);
                vertex = *it;
                // cout << "temp = " << temp << " -> " << vertex << endl;

                if((dist[findPlace(vertex)] > (dist[findPlace(temp)] + edgeDist)) && (!sptSet[findPlace(vertex)])){
                    dist[findPlace(vertex)] = dist[findPlace(temp)] + edgeDist;
                    previousVtx[findPlace(vertex)] = temp;
                }
            }
        }

        vertex = to;

        while(vertex != from){
            vtxTemp = previousVtx[findPlace(vertex)];
            if(edgeExists(vtxTemp, vertex)){
                helper.push(vertex);
                vertex = previousVtx[findPlace(vertex)];
            }
            else{
                result.clear();
                return result;
            }
        }
        
        helper.push(from);

        while(!helper.empty()){
            result.push_back(helper.top());
            helper.pop();
        }

        return result;
    }
     bool edgeExists(const T& from, const T& to){ //check
        int i;
        for( i = 0; i < (int)edges.size(); i++){
            if(((edges[i].from == from) && (edges[i].to == to)) || ((edges[i].from == to) && (edges[i].to == from)&& (isDirected == false))){
            return true;
            }
        }
        return(false);
    }

    // return the place of a node in the adjacency list.
    int findPlace(const T& info) const{
        for(int i = 0; i < (int)adjList.size(); i++){
            if(adjList[i][0] == info){
                return i;
            }
        }
        return -1;
    }

    // Helper method that returns true if a vertex is visited.
    bool ifVisited(const T& info, bool visited[]) const{
        if(visited[findPlace(info)])
            return true;

        return false;
    }

    // static bool sortByDist(const Edge<T> &e1, const Edge<T> &e2) { return e1.dist < e2.dist; }

    // Recursive function to help find the minimum spanning tree.
    void mstHelp(T vertex, list<Edge<T>> &result, bool visited[], list<Edge<T>> helper){
        
        T vtxTemp;

        visited[findPlace(vertex)] = true;

        if(allVisited(visited)){
            result.sort();
            return;
        }

        for(int i = 0; i < (int)edges.size(); i++){
            if((edges[i].from == vertex) || (edges[i].to == vertex)){
                helper.push_back(edges[i]);
            }
        }

        for(auto it = helper.begin(); it != helper.end(); ++it){
            if((ifVisited(it->to, visited)) && (ifVisited(it->from, visited))){
                it = helper.erase(it);
                it--;
            }
        }
        
        helper.sort();
        
        if(vertex == (helper.front()).from){    
            vtxTemp = (helper.front()).to;
        }
        else if(vertex == (helper.front()).to){
            vtxTemp = (helper.front()).from;
        }
        else{
            if(ifVisited((helper.front()).to, visited)){
                vtxTemp = (helper.front()).from;
            }
            else{
                vtxTemp = (helper.front()).to;
            }
        }

        result.push_back(helper.front());
        
        auto it = helper.begin();
        it = helper.erase(it);

        mstHelp(vtxTemp, result, visited, helper);
    }

    // Helper method that returns true if every vertex is visited.
    bool allVisited(bool visited[]){
            for(int j = 0; j < size ; j++){
                if(!visited[j]){
                    return false;
            }
        }
        return true;
    }
    
    int computeDist(const T& from, const T& to){
        int i;
        for(i = 0; i < (int)edges.size(); i++){
            if(((edges[i].from == from) && (edges[i].to == to)) || (((edges[i].from == to) && (edges[i].to == from)) && (isDirected == false))){
                return edges[i].dist;;
            }
        }
        return -1;
    }
    
    T findVtx(int pos){
        int i;
        for(i = 0; i < size; i++){
            if(i == pos){
                return adjList[i][0];
            }
        }
        return adjList[i][0];
    }
    
    T minDist(int dist[], bool sptSet[]){
        int min = numeric_limits<int>::max();
        T index;

        for(int i = 0; i < size; i++){
            if(sptSet[i] == false && dist[i] <= min){
                min = dist[i];
                index = findVtx(i);
            }
        }

        return index;
    }
        //  method to return the distance between "from" and "to".
    

};


#endif
