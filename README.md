# Denom

*A lean framework to build type-safe domain entity identifiers for modern, domain-driven systems.*

---

**Denom** aims to be a lightweight toolkit for working with **domain entity identifiers** – unique, business-relevant identifiers such as employer numbers, 
social security numbers, IBANs, or ISBNs.

Instead of using general value types like strings, numbers or UUIDs, Denom aims to provide type-safe value objects and support for canonicalization. 

**Its goal:** _bring clarity, safety, and consistency to how domain entity identifiers are represented within a system and exchanged across systems._

---

## About the name

The name **Denom** is derived from the word *denomination*, which means "a name or designation" and also refers to "a unit or classification within a system of values".  
This reflects the library’s purpose: giving business entities precise, type-safe identifiers and canonical representations.  
By choosing a concise and memorable name, Denom emphasizes its focus on clarity, semantics, and meaningful identity in domain-driven systems.

---

## Project Goals

- Provide a **common, simple API** for defining custom domain identifiers including support for composite identifiers. 
- Support for **canonicalization** including a default implementation which creates URN/URI compatible (yet human-readable) string representations.
- Keep it **lightweight and framework-agnostic**
---

## Features

- **Type-safe identifiers** as dedicated value objects
- **Default converter** implementation for URN/URI compatible canonical string representations
- **Composite identifiers** for business contexts requiring multiple keys

## Planned Features

- **Validation** for common identifier formats
- **Framework integrations**:
  - JSON (Jackson)
  - JPA/Hibernate converters
  - Bean Validation (Jakarta)
  - OpenAPI schema support

---

## License

This project is licensed under the [Apache License, Version 2.0](LICENSE).
You may not use this project except in compliance with the License.
A copy of the License is included in this repository.
