# Denom

*A lean framework to build type-safe identifiers for modern, domain-driven systems.*

---

**Denom** aims to be a lightweight toolkit for working with **domain identifiers** – unique, business-relevant identifiers such as employer numbers, social security numbers, IBANs, or ISBNs.

Instead of using raw strings or UUIDs, Denom aims to provide type-safe value objects, codecs, validators and integrations for common frameworks.

**Its goal:** _bring clarity, safety, and consistency to how domain identifiers are represented and exchanged across systems._

---

## About the name

The name **Denom** is derived from the word *denomination*, which means "a name or designation" and also refers to "a unit or classification within a system of values".  
This reflects the library’s purpose: giving business entities precise, type-safe identifiers and canonical representations.  
By choosing a concise and memorable name, Denom emphasizes its focus on clarity, semantics, and meaningful identity in domain-driven systems.

---

## Project Goals

- Provide a **common API** for domain identifiers
- Support **canonical and interoperable formats**
- Make it easy to **extend** with custom identifier types
- Keep it **lightweight and framework-agnostic**
---

## Planned Features

- **Type-safe identifiers** as dedicated value objects
- **Codec layer** for canonical string representations (URN/URI compatible)
- **Composite identifiers** for business contexts requiring multiple keys
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
