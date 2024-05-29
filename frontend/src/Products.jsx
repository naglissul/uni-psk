import React, { useState, useEffect } from "react";
import {
  getAllProducts,
  getProductById,
  createProduct,
  updateProduct,
  deleteProduct,
} from "./ProductService";
import "./Products.css";

function Products() {
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState({ name: "", price: "" });
  const [prod, setProd] = useState({ name: "", price: "", opt_lock_num: "" });
  const [searchId, setSearchId] = useState("");
  const [editingProduct, setEditingProduct] = useState({
    id: "",
    name: "",
    price: "",
    opt_lock_num: "",
  });

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    const response = await getAllProducts();
    setProducts(response.data);
  };

  const handleInputChange = (setter) => (event) => {
    const { name, value } = event.target;
    setter((prev) => ({ ...prev, [name]: value }));
  };

  const handleCreate = async (event) => {
    event.preventDefault();
    await createProduct(newProduct);
    setNewProduct({ name: "", price: "" }); // Reset new product form to empty strings
    fetchProducts();
  };

  const handleUpdate = async (event) => {
    event.preventDefault();
    try {
      const updatedProduct = await updateProduct(
        editingProduct.id,
        editingProduct
      );
      setProducts(
        products.map((p) => (p.id === editingProduct.id ? updatedProduct : p))
      );
      setEditingProduct({ id: "", name: "", price: "", opt_lock_num: "" }); // Reset editing product form
      alert("Update successful!");
    } catch (error) {
      if (error.response && error.response.status === 409) {
        alert(
          "Update failed: The product has been modified by someone else. Please refresh and try again."
        );
        fetchProducts(); // Optionally auto-refresh to get the latest data
      } else {
        alert("Update failed: Please try again later.");
      }
    }
  };

  const handleDelete = async (id) => {
    await deleteProduct(id);
    fetchProducts();
  };

  const handleEdit = (product) => {
    setEditingProduct({
      id: product.id,
      name: product.name || "", // Default to empty string if undefined
      price: product.price || "", // Default to empty string if undefined
      opt_lock_num: product.opt_lock_num || "", // Default to empty string if undefined
    });
  };

  const handleSearchById = async () => {
    const response = await getProductById(searchId);
    setProd(response.data);
  };
  return (
    <div>
      <section>
        <h2>Create Product</h2>
        <form onSubmit={handleCreate}>
          <input
            type="text"
            name="name"
            value={newProduct.name}
            onChange={handleInputChange(setNewProduct)}
            placeholder="Name"
          />
          <input
            type="number"
            name="price"
            value={newProduct.price}
            onChange={handleInputChange(setNewProduct)}
            placeholder="Price"
          />
          <button type="submit">Create</button>
        </form>
      </section>

      <section>
        <h2>Edit Product</h2>
        <form onSubmit={handleUpdate}>
          <input
            type="text"
            name="name"
            value={editingProduct.name}
            onChange={handleInputChange(setEditingProduct)}
            placeholder="Name"
          />
          <input
            type="number"
            name="price"
            value={editingProduct.price}
            onChange={handleInputChange(setEditingProduct)}
            placeholder="Price"
          />
          <input
            type="number"
            name="opt_lock_num"
            value={editingProduct.opt_lock_num}
            onChange={handleInputChange(setEditingProduct)}
            placeholder="Optimistic Lock Number"
          />
          <button type="submit" disabled={!editingProduct.id}>
            Update
          </button>
        </form>
      </section>

      <section>
        <h2>Products List</h2>
        <button onClick={fetchProducts}>Refresh Products</button>
        <ul>
          {products.map((prod) => (
            <li key={prod.id}>
              {prod.name} - ${prod.price} - Lock: {prod.opt_lock_num}
              <button onClick={() => handleEdit(prod)}>Edit</button>
              <button onClick={() => handleDelete(prod.id)}>Delete</button>
            </li>
          ))}
        </ul>
      </section>
      <section>
        <h2>Get Product by ID</h2>
        <input
          type="text"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          placeholder="Enter Product ID"
        />
        <button onClick={handleSearchById}>GET by id</button>
        <li>
          {prod.name} - ${prod.price} - Lock: {prod.opt_lock_num}
        </li>
      </section>
    </div>
  );
}

export default Products;
