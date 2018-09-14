// David Mendes Nº44934

class Database {

    var db:array<PersonEntry?>

    var size:nat;

    function RepInv (): bool
        reads this 
    {
        0 < db.Length && 0 <= size <= db.Length
    }
    
    function method notFull (): bool
        reads this
    {
      0 <= size < db.Length
    }

    constructor (capacity : int) 
        requires capacity > 0
        ensures fresh(db)
        ensures RepInv () 
    {
        db := new PersonEntry?[capacity];
        size := 0;
    }

    method save (person : Person) 
        requires RepInv()
        requires notFull()
        modifies this.db, this`size
        ensures RepInv()
    {
        var row := new PersonEntry(person.name, person.age, size);
        db[size] := row;
        size := size + 1;
        
    }

    method resave (person : Person) 
        requires RepInv()
        modifies this.db
        ensures RepInv()
    {
        if (0 <= person.id < db.Length ){
            var row := new PersonEntry(person.name, person.age, person.id);
            db[person.id] := row;
        }
        
    }

    method delete (id : int ) 
        requires RepInv()
        modifies this.db
        ensures RepInv() 
    {
        if (0 <= id < db.Length ){
            db[id] := null;
          }
    }

    method find (id : int ) returns (person : Person?)
        requires RepInv()
        ensures RepInv()
        ensures person != null ==> person.Persistent()
        ensures person != null ==> this == person.dbReference
        ensures this.db == old (this.db)
    {

        if (0 <= id < db.Length) {

            var tableEntry := db[id];

            if (tableEntry != null) {
                var p := new Person (tableEntry.name, tableEntry.age);
                p.id := id;
                p.dbReference := this;
                return p;
            }
        }

        return null;
    }
        
    
}

class Person {
    var id: int;
    var age: int;
    var name: string;
    var dbReference: Database?;

    function method Transient (): bool
        reads this`id, this`dbReference
    {
        id == -1 && dbReference == null
    } 

    function method Persistent (): bool 
        reads this`id, this`dbReference
    {
        id >= 0 && dbReference != null 
    }

    function Detached (): bool 
        reads this`id, this`dbReference
    {
        id >= 0 && dbReference == null
    }

    constructor (name: string, age: int) 
        ensures Transient()
    {
        this.id := -1;
        this.name := name;
        this.age := age;
        dbReference := null;
    }

    // Method save ALWAYS works
    // Even if there is no space in the database it still moves on to the persistent state either way
    // Meaning that when the database is full, because we can't grow the db as we would normally do, persistency is forced 
    // This resolves the issue of the delete functionality not knowing wether the previous save operation left the person in a transient or persistent state
    // It always ensures Persistent () and therefore the delete () will work as that is it's requirement
    method save (database : Database) 
        requires Transient () || Persistent ()
        requires database.RepInv()

        ensures Persistent ()
        ensures database.RepInv()
        ensures dbReference.RepInv()

        ensures database.db == old(database.db)
        ensures this.dbReference.db == old(database.db)

        modifies database.db, this`id, database`size, this`dbReference
    {
        if (database.notFull() && Transient() ){
            dbReference := database;
            this.id := database.size;
            database.save(this);
        }
        else if (Persistent () ) {
            dbReference := database;
            database.resave(this);
        }
        else if (!database.notFull()){
            // force persistency
            this.id := database.size;
            dbReference := database;
        }
        
    } 

    method delete () 
        requires Persistent()
        requires dbReference.RepInv()

        ensures Transient () 
        modifies dbReference.db, this`id, this`dbReference

    {
        dbReference.delete(this.id);
        this.id := -1;
        dbReference := null;
        
    }

    method close ()
        requires Persistent() 
        ensures Detached()
        modifies this`dbReference 
    {
        dbReference := null;
    }

    method update (database: Database) 
        requires Detached ()
        ensures Persistent () 
        modifies this`dbReference
    {
        dbReference := database;
    }

    method getName () returns (name : string) 

    {
        return this.name;
    }

    method getAge () returns (age : int) 
    
    {
        return this.age;
    }

    
    method setAge (newAge : int ) 
        modifies this`age 
    {
        this.age := newAge;
    }

    method setName (newName : string ) 
        modifies this`name  
    {
        this.name := newName;
    }

}

class PersonEntry {
  
    var age: int;
    
    var name: string;
    
    var id : int;

    constructor (name: string, age: int, id: int) 
    {    
        this.name := name;
        this.age := age;
        this.id := id;
    }
    
}

method Main () {
    var database := new Database (8);
    var user1 := new Person ("John", 20);
    var user2 := new Person ("Doe", 25);
      
    user1.save(database);   

    user1.setAge(22);
    user1.setName("Camelia");

    user1.save(database);

    user2.save(database);
    
    var p1 := database.find(0);
   
    user1.delete();

    if (p1 != null) {
        p1.save(database);
    }


    
}